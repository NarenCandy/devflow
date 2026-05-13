package com.devflow.devflow.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String email;

    public UserDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

}
