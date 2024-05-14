package com.example.shopping_app.controller;

import com.example.shopping_app.convert.OrderDetailResConverter;
import com.example.shopping_app.dto.OrderDTO;
import com.example.shopping_app.dto.OrderDetailDTO;
import com.example.shopping_app.entity.OrderDetail;
import com.example.shopping_app.response.OrderDetailResponse;
import com.example.shopping_app.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final OrderDetailResConverter orderDetailResConverter;

    @PostMapping("")
    public ResponseEntity<?> insertOrderDetail(
            @Valid
            @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errMessage= result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMessage);
            }
            OrderDetail newOrderDetail= orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(orderDetailResConverter.toResponse(newOrderDetail));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
        try{
            OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok(orderDetailResConverter.toResponse(orderDetail));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody OrderDetailDTO orderDetailDTO) {

        try{
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(orderDetailDTO, id);
            return ResponseEntity.ok(orderDetail);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(
             @PathVariable("id") Long id
            ) {

        try{
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok("Delete order detail successfully");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
