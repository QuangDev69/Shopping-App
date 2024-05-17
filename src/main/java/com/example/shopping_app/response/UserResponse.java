package com.example.shopping_app.response;

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
