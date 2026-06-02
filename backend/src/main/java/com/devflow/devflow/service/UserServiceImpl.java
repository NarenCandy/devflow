package com.devflow.devflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devflow.devflow.dto.UserDTO;
import com.devflow.devflow.model.User;
import com.devflow.devflow.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserRepository userRepository;

    private User getCurrentUserEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDTO getCurrentUser() {
        User user = getCurrentUserEntity();
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public UserDTO updateUser(UserDTO dto) {
        User user = getCurrentUserEntity();
        if (dto.getName() != null) user.setName(dto.getName());
        return new UserDTO(
            userRepository.save(user).getId(),
            userRepository.save(user).getName(),
            userRepository.save(user).getEmail()
        );
    }
}