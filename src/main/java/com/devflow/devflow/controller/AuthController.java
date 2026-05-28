package com.devflow.devflow.controller;

import java.security.Principal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devflow.devflow.dto.UserDTO;
import com.devflow.devflow.dto.UserUpdateRequestDTO;
import com.devflow.devflow.model.User;
import com.devflow.devflow.repository.UserRepository;
import com.devflow.devflow.util.JwtUtil;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder; 


    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    public String register(@RequestBody @Valid User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User existingUser = userRepository.findByEmail(user.getUsername())
        .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }
        
        return jwtUtil.generateToken(existingUser);
    }
    @GetMapping("/me")
    public UserDTO getCurrentUser(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user.getId(), user.getName(), user.getEmail());

    }

    @PutMapping("/update")
    public UserDTO updateUser(@RequestBody @Valid UserUpdateRequestDTO request, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if(request.getName() != null && !request.getName().isEmpty()){
            user.setName(request.getName());
        }
        if(request.getPassword() != null && !request.getPassword().isBlank()){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);
        return new UserDTO(user.getId(), user.getName(), user.getEmail());



    }
    
}
