package com.example.shopping_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "Phone Number is required!")
    private String phoneNumber;
    @NotBlank(message = "Password cannot be blank!")
    private String password;

}
