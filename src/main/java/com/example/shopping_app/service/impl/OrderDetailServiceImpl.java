package com.example.shopping_app.service.impl;

import com.example.shopping_app.Exceptional.DataNotFoundException;
import com.example.shopping_app.convert.OrderDetailConverter;
import com.example.shopping_app.dto.OrderDetailDTO;
import com.example.shopping_app.entity.Order;
import com.example.shopping_app.entity.OrderDetail;
import com.example.shopping_app.entity.Product;
import com.example.shopping_app.repository.OrderDetailRepository;
import com.example.shopping_app.repository.OrderRepository;
import com.example.shopping_app.repository.ProductRepository;
import com.example.shopping_app.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderDetailConverter orderDetailConverter;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found with ID: " + orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found with ID: " + orderDetailDTO.getProductId()));

        OrderDetail newOrderDetail = orderDetailConverter.toEntity(orderDetailDTO);
        newOrderDetail.setOrder(order);
        newOrderDetail.setProduct(product);

        return orderDetailRepository.save(newOrderDetail);
    }


    @Override
    public OrderDetail getOrderDetail(Long id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    @Override
    public OrderDetail updateOrderDetail(OrderDetailDTO orderDetailDTO, Long id) {
        OrderDetail existOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order detail not found with ID: " + id));

        Order existOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                    .orElseThrow(() -> new DataNotFoundException("Order not found with ID: " + orderDetailDTO.getOrderId()));

          Product  existProduct = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Product not found with ID: " + orderDetailDTO.getProductId()));
        System.out.println("existOrderDetail.getId(): "+existOrderDetail.getId());
        orderDetailConverter.updateEntity(orderDetailDTO, existOrderDetail);
        existOrderDetail.setOrder(existOrder);
        existOrderDetail.setProduct(existProduct);
        return orderDetailRepository.save(existOrderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
