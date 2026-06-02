package com.devflow.devflow.dto;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private String message;
    private int statusCode;

    public ErrorResponseDTO(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
