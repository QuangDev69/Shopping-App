package com.example.shopping_app.service;

import com.example.shopping_app.Exceptional.DataNotFoundException;
import com.example.shopping_app.dto.UserDTO;
import com.example.shopping_app.entity.User;
import com.example.shopping_app.response.UserResponse;

public interface UserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber, String password);

    UserResponse getUserDetail(String token);
}
