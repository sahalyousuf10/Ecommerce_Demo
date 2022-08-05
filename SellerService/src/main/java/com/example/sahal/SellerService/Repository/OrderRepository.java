package com.example.sahal.SellerService.Repository;

import com.example.sahal.SellerService.Model.Orders;
import com.example.sahal.SellerService.Model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
}
