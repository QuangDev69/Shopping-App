package com.example.shopping_app.service.impl;

import com.example.shopping_app.Exceptional.DataNotFoundException;
import com.example.shopping_app.dto.UserDTO;
import com.example.shopping_app.entity.Role;
import com.example.shopping_app.entity.User;
import com.example.shopping_app.middleware.JwtUtil;
import com.example.shopping_app.repository.RoleRepository;
import com.example.shopping_app.repository.UserRepository;
import com.example.shopping_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    //Register User
    public User createUser(UserDTO userDTO) {
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role newRole = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Don't have Role..."));

        User newUser = User.builder()
                .fullName(userDTO.getFullname())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .role(newRole) // Thiết lập Role ngay khi tạo
                .build();
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        userRepository.save(newUser); // Lưu User mới vào cơ sở dữ liệu
        return newUser;
    }

    @Override
    public String login(String phoneNumber, String password) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Invalid phone number / password");
        }
        User existingUser = optionalUser.get();
        if (existingUser.getFacebookAccountId() !=0 || existingUser.getGoogleAccountId() != 0) {
            return jwtUtil.generateToken(existingUser);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
            if (authentication.isAuthenticated()) {
                return jwtUtil.generateToken(existingUser);
            } else throw new BadCredentialsException("Wrong phone number or password!");
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Authentication failed", e);
        }
    }
}
