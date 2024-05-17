package com.example.shopping_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    @NotBlank(message = "Fullname is required!")
    private String fullName;

    @NotBlank(message = "Phone Number is required!")
    private String phoneNumber;
    private String address;
    @NotBlank(message = "Password cannot be blank!")
    private String password;

    @NotBlank(message = "Confirm Password cannot be blank!")
    private String confirmPassword;

    private Date dateOfBirth;
    private int facebookAccountId;
    private int googleAccountId;

    @NotNull(message = "RoleId cannot be blank!")
    private Long roleId;

}
