package com.example.sources.repo;

import com.example.sources.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderEntityRepo extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByOwner_Account_Id(Long accountId);
}
