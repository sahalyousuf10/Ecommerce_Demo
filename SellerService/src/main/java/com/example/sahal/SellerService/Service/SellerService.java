package com.example.sahal.SellerService.Service;

import com.example.sahal.SellerService.Dto.ErrorDto;
import com.example.sahal.SellerService.Dto.ProductDto;
import com.example.sahal.SellerService.Feign.InventoryFeignService;
import com.example.sahal.SellerService.Model.Orders;
import com.example.sahal.SellerService.Model.Seller;
import com.example.sahal.SellerService.Repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class SellerService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryFeignService inventoryFeignService;

    private static final String FILE_1_PATH = "/Users/msahal/Documents/workspace/Record.txt";

    @Async
    public CompletableFuture<ProductDto> buyProductByName(String name, long ccNo, String productName, int quantity) throws IOException {

        CompletableFuture completableFuture = new CompletableFuture();
        try {
            ProductDto productDto = inventoryFeignService.buyProductByName(productName, quantity).getBody();
            Seller seller = new Seller(1, "Arham");
            if (productDto != null && productDto.getErrors() == null) {
                FileWriter fw1 = new FileWriter(FILE_1_PATH, true);
                fw1.append("Product name: "+productDto.getName()+
                        ", Product price: "+productDto.getPrice()+
                        ", Quantity: "+quantity+
                        ", Total price: "+productDto.getPrice()*quantity+
                        ", Buyer name: "+name+
                        ", Seller name: "+seller.getName()+
                        ", Time: "+new Date()+"\n");
                fw1.close();
                Orders orders = new Orders();
                orders.setProductName(productDto.getName());
                orders.setBuyerName(name);
                orders.setSellerName(seller.getName());
                orders.setPrice(productDto.getPrice());
                orders.setQuantity(quantity);
                orders.setTotalPrice(productDto.getPrice()*quantity);
                orders.setTimeStamp(new Date());
                orderRepository.save(orders);
                return completableFuture.completedFuture(productDto);
            }
            else if (productDto.getErrors() != null) {
                ProductDto productDto1 = new ProductDto();
                ErrorDto error = new ErrorDto();
                error.setTimeStamp(productDto1.getErrors().getTimeStamp());
                error.setMessage(productDto1.getErrors().getMessage());
                error.setDetails(productDto1.getErrors().getDetails());
                productDto1.setErrors(error);
                return completableFuture.completedFuture(productDto1);
            }
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
        return completableFuture;
    }
}

