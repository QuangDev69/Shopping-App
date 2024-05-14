package com.example.shopping_app.convert;

import com.example.shopping_app.dto.OrderDTO;
import com.example.shopping_app.dto.OrderDetailDTO;
import com.example.shopping_app.entity.Order;
import com.example.shopping_app.entity.OrderDetail;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class OrderDetailConverter {
    private final TypeMap<OrderDetailDTO, OrderDetail> typeMap;

    @Autowired
    public OrderDetailConverter(ModelMapper modelMapper) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.typeMap = modelMapper.createTypeMap(OrderDetailDTO.class, OrderDetail.class);

        // Rõ ràng chỉ định ánh xạ và sử dụng skip() để ngăn không cho setId được gọi
        this.typeMap.addMappings(mapper -> mapper.skip(OrderDetail::setId));

        //        this.typeMap.addMappings(mapper -> {
        //            mapper.map(OrderDetailDTO::getOrderId, OrderDetail::setOrder);
        //            mapper.map(OrderDetailDTO::getProductId, OrderDetail::setProduct);
        //            mapper.skip(OrderDetail::setId);
        //        });
    }


    public OrderDetail toEntity(OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = new OrderDetail();
        this.typeMap.map(orderDetailDTO, orderDetail);
        return orderDetail;
    }

    public void updateEntity(OrderDetailDTO orderDetailDTO, OrderDetail orderDetail) {
        this.typeMap.map(orderDetailDTO, orderDetail);
    }
}
