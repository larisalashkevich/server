package com.example.sources.model.request.card_item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCardItemRequest {
    private Long id;
    private Integer count;
}
