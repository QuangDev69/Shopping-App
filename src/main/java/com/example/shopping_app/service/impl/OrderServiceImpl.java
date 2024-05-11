package com.example.shopping_app.service.impl;

import com.example.shopping_app.Exceptional.DataNotFoundException;
import com.example.shopping_app.convert.OrderConverter;
import com.example.shopping_app.dto.OrderDTO;
import com.example.shopping_app.entity.Order;
import com.example.shopping_app.entity.User;
import com.example.shopping_app.repository.OrderRepository;
import com.example.shopping_app.repository.UserRepository;
import com.example.shopping_app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderConverter orderConverter;
    private final OrderRepository orderRepository;
    @Override
    public Order createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Not found User with id: "+orderDTO.getUserId()));
        Order newOrder = orderConverter.toEntity(orderDTO);
        newOrder.setUser(existingUser);
        newOrder.setOrderDate(new Date());
        newOrder.setStatus("PENDING");
        newOrder.setActive(true);
        Date shippingDate = orderDTO.getShippingDate() == null ? new Date() : orderDTO.getShippingDate();
        if(shippingDate == null || shippingDate.before(new Date())){
            throw new DataNotFoundException("Shipping Date must be at least today ");
        }
        newOrder.setShippingDate(shippingDate);
        orderRepository.save(newOrder);
        return null;
    }

    @Override
    public Order getOrderById(Long id) {
        return null;
    }

    @Override
    public Order updateOrder(OrderDTO orderDTO, Long id) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {

    }

    @Override
    public List<Order> getAllOrder(Long userId) {
        return null;
    }
}
