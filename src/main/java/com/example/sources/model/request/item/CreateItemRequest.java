package com.example.sources.model.request.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateItemRequest {
    private Long accountId;
    private String name;
    private Double price;
    private String imagePath;
    private String description;
    private Long categoryId;
}
