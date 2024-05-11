package com.example.shopping_app.controller;

import com.example.shopping_app.dto.OrderDTO;
import com.example.shopping_app.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> insertOrder(
            @Valid
            @RequestBody OrderDTO orderDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errMessage= result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMessage);
            }

            orderService.createOrder(orderDTO);
            return ResponseEntity.ok("Create order "+orderDTO.getFullname()+"successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> getOrders(@Valid @PathVariable("userId") Long userId) {
        try{
            return ResponseEntity.ok("Get orders success");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateOrder(
            @Valid @PathVariable("userId") Long userId,
            @Valid OrderDTO orderDTO) {

        try{
            return ResponseEntity.ok("Get orders success");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteOrder(
            @Valid @PathVariable("userId") Long userId,
            @Valid OrderDTO orderDTO) {

        try{
            return ResponseEntity.ok("Get orders success");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
