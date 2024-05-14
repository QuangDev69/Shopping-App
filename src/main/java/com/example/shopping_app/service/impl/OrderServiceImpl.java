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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderConverter orderConverter;
    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(OrderDTO orderDTO)  {
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Not found User with id: "+orderDTO.getUserId()));
        Order newOrder = orderConverter.toEntity(orderDTO);
        newOrder.setUser(existingUser);
        newOrder.setOrderDate(new Date());
        newOrder.setStatus("PENDING");
        newOrder.setIsActive(true);
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate == null || shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Shipping Date must be at least today ");
        }
        newOrder.setShippingDate(shippingDate);
        orderRepository.save(newOrder);
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
