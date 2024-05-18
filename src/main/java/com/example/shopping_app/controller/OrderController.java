package com.example.shopping_app.controller;

import com.example.shopping_app.dto.OrderDTO;
import com.example.shopping_app.entity.Order;
import com.example.shopping_app.response.OrderListResponse;
import com.example.shopping_app.response.OrderResponse;
import com.example.shopping_app.service.OrderService;
import com.example.shopping_app.util.LocalizationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final LocalizationUtil localizationUtil;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")

    public ResponseEntity<?> insertOrder(
            @Valid
            @RequestBody OrderDTO orderDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMessage);
            }

            Order newOrder = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(newOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getOrders(@Valid @PathVariable("userId") Long userId) {
        try {
            List<Order> ordersByUser = orderService.findByUserId(userId);
            return ResponseEntity.ok(ordersByUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getOrderById(@Valid @PathVariable Long orderId) {
        try {
            Order orderById = orderService.getOrderById(orderId);
            return ResponseEntity.ok(orderById);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable("orderId") Long orderId,
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> error = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(error);

        }
        try {
            Order exitOrder = orderService.getOrderById(orderId);
            if (exitOrder.getIsActive()) {
                Order updateOrder = orderService.updateOrder(orderDTO, orderId);
                return ResponseEntity.ok(updateOrder);
            } else {
                return ResponseEntity.badRequest().body("Not found");

            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOrder(
            @PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Delete orders success");
    }

    @GetMapping("/getAllOrders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderListResponse> getOrders(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());

        Page<OrderResponse> responsePage = orderService
                .findOrderByKeyword(keyword, pageRequest)
                .map(OrderResponse::fromOrder);
        System.out.println(" Page<OrderResponse: "+responsePage);
        int totalPage = responsePage.getTotalPages();
        System.out.println("totalPage: "+totalPage);
        List<OrderResponse> orderResponses = responsePage.getContent();

        return ResponseEntity.ok(OrderListResponse.builder().orders(orderResponses).totalPages(totalPage).build());

    }



}
