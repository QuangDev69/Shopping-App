package com.example.shopping_app.convert;


import com.example.shopping_app.entity.OrderDetail;
import com.example.shopping_app.entity.User;
import com.example.shopping_app.response.OrderDetailResponse;
import com.example.shopping_app.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserConverter {
    private final TypeMap<User, UserResponse> typeMap;
    @Autowired
    public UserConverter(ModelMapper modelMapper) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.typeMap = modelMapper.createTypeMap(User.class, UserResponse.class);

        this.typeMap.addMappings(mapper -> {
            mapper.map(src -> src.getRole().getId(), UserResponse::setRoleId);
        });

    }
    public UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        this.typeMap.map(user, userResponse);
        return userResponse;
    }


}
