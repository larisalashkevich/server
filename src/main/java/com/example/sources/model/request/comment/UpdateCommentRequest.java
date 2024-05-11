package com.example.sources.model.request.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCommentRequest {
    private Long id;
    private String text;
}
