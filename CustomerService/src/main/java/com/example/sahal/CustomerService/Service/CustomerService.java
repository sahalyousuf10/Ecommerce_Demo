package com.example.sahal.CustomerService.Service;

import com.example.sahal.CustomerService.Dto.ErrorDto;
import com.example.sahal.CustomerService.Dto.ProductDto;
import com.example.sahal.CustomerService.Feign.SellerFeignService;
import com.example.sahal.CustomerService.Repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerFeignService sellerFeignService;

    @Async
    public CompletableFuture<List<ProductDto>> buyProductByName(String name, long CCNo, String productName, int quantity, MultipartFile file) throws Exception {
        CompletableFuture completableFuture = new CompletableFuture();
        List<ProductDto> productList = new ArrayList<>();
        try {
            List<ProductDto> orderList = parseCSVFile(file);
            for (ProductDto product : orderList){
                ProductDto productDto = sellerFeignService.buyProductByName(name, CCNo, product.getName(), product.getQuantity()).getBody();
                if (productDto != null && productDto.getErrors() == null) {
                    productList.add(productDto);
                    completableFuture.completedFuture(productList);
                }
                else if (productDto.getErrors() != null) {
                    ProductDto productDto1 = new ProductDto();
                    ErrorDto error = new ErrorDto();
                    error.setTimestamp(productDto2.getErrors().getTimestamp());
                    error.setMessage(productDto2.getErrors().getMessage());
                    error.setDetails(productDto2.getErrors().getDetails());
                    productDto2.setErrors(error);
                    completableFuture.completedFuture(productDto2);
                }
            }
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
        return completableFuture;
    }

    private List<ProductDto> parseCSVFile(MultipartFile file) throws Exception{
        final List<ProductDto> orderList = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final ProductDto product = new ProductDto();
                    product.setName(data[0]);
                    product.setQuantity(Integer.parseInt(data[1]));
                    orderList.add(product);
                }
                return orderList;
            }
        }
        catch (Exception ex){
            log.error("Failed to parse CSV file "+ex.getMessage());
            throw ex;
        }
    }
}
