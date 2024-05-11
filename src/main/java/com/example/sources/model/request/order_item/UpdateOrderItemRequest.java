package com.example.sources.model.request.order_item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateOrderItemRequest {
    private Long id;
    private String state;
}
