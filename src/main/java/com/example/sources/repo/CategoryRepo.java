package com.example.sources.repo;

import com.example.sources.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
