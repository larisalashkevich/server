package com.example.sources.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemNotFoundException extends Exception{
    private String message;
}
