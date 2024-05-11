package com.example.sources.repo;

import com.example.sources.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByItem_Creator_Account_Id(Long id);
}
