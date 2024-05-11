package com.example.shopping_app.convert;

import com.example.shopping_app.dto.OrderDTO;
import com.example.shopping_app.entity.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConverter {
    private final TypeMap<OrderDTO, Order> typeMap;

    @Autowired
    public OrderConverter(ModelMapper modelMapper) {
        this.typeMap = modelMapper.createTypeMap(OrderDTO.class, Order.class);
        // Specify fields to skip
        this.typeMap.addMappings(mapper -> mapper.skip(Order::setId));
    }

    public Order toEntity(OrderDTO orderDTO) {
        Order order = new Order();
        this.typeMap.map(orderDTO, order);
        return order;
    }
}
