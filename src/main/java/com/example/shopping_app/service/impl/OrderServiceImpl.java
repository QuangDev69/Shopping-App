package com.example.shopping_app.service.impl;

import com.example.shopping_app.Exceptional.DataNotFoundException;
import com.example.shopping_app.convert.OrderConverter;
import com.example.shopping_app.dto.CartItemDTO;
import com.example.shopping_app.dto.OrderDTO;
import com.example.shopping_app.entity.Order;
import com.example.shopping_app.entity.OrderDetail;
import com.example.shopping_app.entity.Product;
import com.example.shopping_app.entity.User;
import com.example.shopping_app.repository.OrderDetailRepository;
import com.example.shopping_app.repository.OrderRepository;
import com.example.shopping_app.repository.ProductRepository;
import com.example.shopping_app.repository.UserRepository;
import com.example.shopping_app.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderConverter orderConverter;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng với ID: " + orderDTO.getUserId()));

        Order newOrder = orderConverter.toEntity(orderDTO);
        newOrder.setUser(existingUser);
        newOrder.setOrderDate(new Date());
        newOrder.setStatus("PENDING");
        newOrder.setIsActive(true);
        LocalDate shippingDate = orderDTO.getShippingDate() != null ? orderDTO.getShippingDate() : LocalDate.now();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày giao hàng phải là hôm nay hoặc sau hôm nay.");
        }
        newOrder.setShippingDate(shippingDate);
        orderRepository.save(newOrder);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItem : orderDTO.getCartItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với ID: " + cartItem.getProductId()));
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(cartItem.getQuantity());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return newOrder;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Not Found Order witd id: "+id));
    }

    @Override
    public Order updateOrder(OrderDTO orderDTO, Long id) {
        Order orderExisting = orderRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Not Found Order witd id: "+id));
        orderConverter.updateEntity(orderDTO, orderExisting);
        return orderRepository.save(orderExisting);
    }

    @Override
    public void deleteOrder(Long id) {
        Order orderExisting = orderRepository.findById(id).orElse(null);
        if(orderExisting != null) {
            orderExisting.setIsActive(false);
            orderRepository.save(orderExisting);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {

        return orderRepository.findByUserId(userId);
    }
}
