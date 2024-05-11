package com.example.sources.model.request.card_item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCardItemRequest {
    private Long accountId;
    private Long itemId;
    private Integer count;
}
