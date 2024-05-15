package com.example.shopping_app.response;

import com.example.shopping_app.entity.User;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String message;
    private User user;
}
