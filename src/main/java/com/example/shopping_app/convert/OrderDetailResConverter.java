package com.example.shopping_app.convert;

import com.example.shopping_app.dto.OrderDetailDTO;
import com.example.shopping_app.entity.OrderDetail;
import com.example.shopping_app.response.OrderDetailResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailResConverter {
    private final TypeMap<OrderDetail, OrderDetailResponse> typeMap;

    @Autowired
    public OrderDetailResConverter(ModelMapper modelMapper) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.typeMap = modelMapper.createTypeMap(OrderDetail.class, OrderDetailResponse.class);

        this.typeMap.addMappings(mapper -> {
            mapper.map(src -> src.getOrder().getId(), OrderDetailResponse::setOrderId);
            mapper.map(src -> src.getProduct().getId(), OrderDetailResponse::setProductId);
        });

    }


    public OrderDetailResponse toResponse(OrderDetail orderDetail) {
        OrderDetailResponse orderDetailRes = new OrderDetailResponse();
        this.typeMap.map(orderDetail, orderDetailRes);
        return orderDetailRes;
    }

}
