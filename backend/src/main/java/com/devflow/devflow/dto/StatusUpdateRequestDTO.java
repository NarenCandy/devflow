package com.devflow.devflow.dto;

import lombok.Data;

@Data
public class StatusUpdateRequestDTO {
    private String status;

    

    public StatusUpdateRequestDTO(String status) {
        this.status = status;
    }
}

