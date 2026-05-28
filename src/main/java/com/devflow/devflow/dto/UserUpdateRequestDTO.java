package com.devflow.devflow.dto;

import lombok.Data;

@Data
public class UserUpdateRequestDTO {
    private String name;
    private String password;

    public UserUpdateRequestDTO(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
