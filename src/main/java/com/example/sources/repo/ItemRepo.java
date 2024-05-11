package com.example.sources.repo;

import com.example.sources.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findByCreator_Account_Id(Long id);
}
