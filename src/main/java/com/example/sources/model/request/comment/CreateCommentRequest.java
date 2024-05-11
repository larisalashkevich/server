package com.example.sources.model.request.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCommentRequest {
    private Long accountId;
    private Long itemId;
    private String text;
}
