package com.example.sources.model.response.category;

import com.example.sources.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Hibernate;
@Data
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private Integer itemsCount;

    public CategoryResponse(Category category){
        Hibernate.initialize(category.getItems());

        this.id = category.getId();
        this.name = category.getName();
        this.itemsCount = category.getItems() == null? 0 : category.getItems().size();
    }
}
