package com.example.sources.model.response.item;

import com.example.sources.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Hibernate;

import java.util.Date;

@Data
@AllArgsConstructor
public class ItemResponse {
    private Long id;

    private String name;
    private Double price;
    private String description;
    private Date created;
    private Boolean isDeleted;

    private String imagePath;

    private Long categoryId;
    private String category;

    private Long profileInfoId;
    private Long accountId;

    public ItemResponse(Item item){
        Hibernate.initialize(item.getCategory());
        Hibernate.initialize(item.getCreator());
        Hibernate.initialize(item.getCreator().getAccount());

        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.description = item.getDescription();
        this.created = item.getCreated();
        this.isDeleted = item.getIsDeleted();
        this.imagePath = item.getImagePath() == null? null : "http://127.0.0.1:8080/api/img/" + item.getImagePath();
        this.categoryId = item.getCategory().getId();
        this.category = item.getCategory().getName();
        this.profileInfoId = item.getCreator().getId();
        this.accountId = item.getCreator().getAccount().getId();
    }
}
