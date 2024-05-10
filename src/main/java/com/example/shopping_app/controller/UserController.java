package com.example.shopping_app.controller;

import com.example.shopping_app.dto.UserDTO;
import com.example.shopping_app.dto.UserLoginDTO;
import com.example.shopping_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errMessage= result.getFieldErrors()
                                                .stream()
                                                .map(FieldError::getDefaultMessage)
                                                .toList();
                return ResponseEntity.badRequest().body (errMessage);
            }
            if(!userDTO.getPassword().equals(userDTO.getConfirmPassword())){
                return ResponseEntity.badRequest().body("Not success");

            }
            userService.createUser(userDTO);
            return ResponseEntity.ok("Register successfully");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@Valid @RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok("Success");
    }
}
