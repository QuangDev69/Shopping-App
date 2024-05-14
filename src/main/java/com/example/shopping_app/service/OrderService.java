package com.example.shopping_app.service;

import com.example.shopping_app.Exceptional.DataNotFoundException;
import com.example.shopping_app.dto.OrderDTO;
import com.example.shopping_app.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(OrderDTO orderDTO) throws DataNotFoundException;

    Order getOrderById(Long id);

    Order updateOrder(OrderDTO orderDTO, Long id);

    void deleteOrder(Long id);

    List<Order> findByUserId(Long userId);
}
