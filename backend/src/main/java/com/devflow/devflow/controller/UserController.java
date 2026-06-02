package com.devflow.devflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devflow.devflow.dto.UserDTO;
import com.devflow.devflow.repository.UserRepository;

@RestController
public class UserController {

    @Autowired private UserRepository userRepository;


    @GetMapping("/users/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String q) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserDTO> results = userRepository.findAll().stream()
                .filter(u -> !u.getEmail().equals(currentEmail))
                .filter(u -> u.getName().toLowerCase().contains(q.toLowerCase())
                        || u.getEmail().toLowerCase().contains(q.toLowerCase()))
                .map(u -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(u.getId());
                    dto.setName(u.getName());
                    dto.setEmail(u.getEmail());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(results);
    }
}