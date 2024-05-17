package com.example.shopping_app.response;

import com.example.shopping_app.convert.OrderDetailResConverter;
import com.example.shopping_app.convert.UserConverter;
import com.example.shopping_app.entity.Role;
import com.example.shopping_app.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserResponse {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String address;
    private boolean isActive;
    private Date dateOfBirth;
    private int facebookAccountId;
    private int googleAccountId;
    private int roleId;

}
