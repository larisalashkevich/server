package com.example.sources.model.request.category;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCategoryRequest {
    private Long id;
    private String name;
}
