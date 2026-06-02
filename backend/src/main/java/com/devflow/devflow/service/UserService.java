package com.devflow.devflow.service;

import com.devflow.devflow.dto.UserDTO;

public interface UserService {
    UserDTO getCurrentUser();
    UserDTO updateUser(UserDTO dto);

}
