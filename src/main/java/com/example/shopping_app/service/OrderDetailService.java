package com.example.shopping_app.service;

import com.example.shopping_app.dto.OrderDetailDTO;
import com.example.shopping_app.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO);

    OrderDetail getOrderDetail(Long id);

    OrderDetail updateOrderDetail(OrderDetailDTO orderDetailDTO, Long id);

    void deleteOrderDetail(Long id);

    List<OrderDetail> findByOrderId(Long orderId);

}
