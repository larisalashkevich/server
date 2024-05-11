package com.example.sources.model.request.category;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCategoryRequest {
    private Long id;
    private String name;
}
