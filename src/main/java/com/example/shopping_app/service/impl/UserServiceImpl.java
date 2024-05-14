package com.example.shopping_app.service.impl;

import com.example.shopping_app.Exceptional.DataNotFoundException;
import com.example.shopping_app.dto.UserDTO;
import com.example.shopping_app.entity.Role;
import com.example.shopping_app.entity.User;
import com.example.shopping_app.repository.RoleRepository;
import com.example.shopping_app.repository.UserRepository;
import com.example.shopping_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public User createUser(UserDTO userDTO)  {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role newRole = roleRepository.findById(userDTO.getRoleId()).orElseThrow(()-> new DataNotFoundException("Don't have Role..."));

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
        userRepository.save(newUser); // Lưu User mới vào cơ sở dữ liệu
        return newUser;
    }

    @Override
    public String login(String phoneNumber, String password) {
        return null;
    }
}
