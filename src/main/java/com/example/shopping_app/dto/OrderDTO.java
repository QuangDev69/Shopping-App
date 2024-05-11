package com.example.shopping_app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @Min(value = 1, message = "User's   Id must be > 0 ")
    private Long userId;
    private String fullname;

    @Email(message = "Email must be in correct format")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Phone number not blank")
    @Size(min=5, message = "Phone number must be > 5 characters")
    private String phoneNumber;
    private String address;

    @Min(value = 0, message = "Total money must be >= 0")
    private Float totalMoney;

    @NotBlank(message = "Shipping Method  not blank")
    private String shippingMethod;

    @NotBlank(message = "Shipping Address not blank")
    private String shippingAddress;

    @NotNull(message = "Shipping date must not be null")
    private Date shippingDate;


    @NotBlank(message = "Payment Method not blank")
    private String paymentMethod;

}
