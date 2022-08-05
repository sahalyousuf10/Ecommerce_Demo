package com.example.sahal.SellerService.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
public class Orders {

    @Id
    @GeneratedValue
    private long orderId;
    private String buyerName;
    private String productName;
    private int quantity;
    private long price;
    private long totalPrice;
    private String sellerName;
    private Date timeStamp;
}
