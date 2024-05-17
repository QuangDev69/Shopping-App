package com.example.shopping_app.controller;

import com.example.shopping_app.Exceptional.DataNotFoundException;
import com.example.shopping_app.dto.UserDTO;
import com.example.shopping_app.dto.UserLoginDTO;
import com.example.shopping_app.entity.User;
import com.example.shopping_app.response.LoginResponse;
import com.example.shopping_app.response.UserResponse;
import com.example.shopping_app.service.UserService;
import com.example.shopping_app.util.LocalizationUtil;
import com.example.shopping_app.util.MessageKeyUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final UserService userService;
    private final LocalizationUtil localizationUtil;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }

        try {
            User createdUser = userService.createUser(userDTO);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String multiMessage = localizationUtil.setLocaleMessage(MessageKeyUtil.LOGIN_SUCCESS);
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            return ResponseEntity.ok(LoginResponse.builder()
                    .message(multiMessage)
                    .token(token)
                    .build());
        }
        catch (Exception e) {
            String messFailed = localizationUtil.setLocaleMessage(MessageKeyUtil.LOGIN_FAILED, e.getMessage());
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(messFailed)
                    .build());
        }
    }

    @GetMapping("/{userId}/details")
    public ResponseEntity<UserResponse> getUserDetail(@RequestHeader("Authorization") String token){
        try {
            String extractToken = token.substring(7);
            UserResponse user = userService.getUserDetail(extractToken);
            return ResponseEntity.ok(user);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping ("/{userId}/details")
    public ResponseEntity<?> getUserDetail(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UserDTO userDTO,
            @PathVariable Long userId
            ){
        try {

            String extractToken = token.substring(7);
            UserResponse user = userService.getUserDetail(extractToken);
            if(!user.getId().equals(userId)){
                return ResponseEntity.badRequest().body("Id does not match");
            }
            UserResponse updateUser = userService.updateUser(userId, userDTO);
            return ResponseEntity.ok(updateUser);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
