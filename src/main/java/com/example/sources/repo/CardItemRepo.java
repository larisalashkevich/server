package com.example.sources.repo;

import com.example.sources.domain.CardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CardItemRepo extends JpaRepository<CardItem, Long> {
    List<CardItem> findByOwner_Account_Id(Long id);
}
