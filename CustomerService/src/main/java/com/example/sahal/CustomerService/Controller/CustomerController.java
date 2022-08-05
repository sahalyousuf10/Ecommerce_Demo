package com.example.sahal.CustomerService.Controller;

import com.example.sahal.CustomerService.Dto.ProductDto;
import com.example.sahal.CustomerService.Service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "/product", produces = "application/json")
    public ResponseEntity<ProductDto> buyProductByName(
            @RequestParam(value = "Name") String name,
            @RequestParam(value = "CCNo") long CCNo,
            @RequestParam(value = "Product Name") String productName,
            @RequestParam(value = "Quantity") int quantity,
            @RequestParam(value = "Order File") MultipartFile file) throws Exception {
        ProductDto productDto = new ProductDto();
        try {
            productDto = customerService.buyProductByName(name, CCNo, productName, quantity, file).get();
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw new Exception("Product with name "+productName+" not found due to some internal error!");
        }
        return ResponseEntity.ok(productDto);
                //.status(HttpStatus.FOUND)
                //.body(productDto);
    }
}
