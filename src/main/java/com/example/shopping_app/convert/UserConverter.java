package com.example.shopping_app.convert;

import com.example.shopping_app.dto.UserDTO;
import com.example.shopping_app.entity.User;
import com.example.shopping_app.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    private final TypeMap<UserDTO, User> userTypeMap;
    private final TypeMap<User, UserResponse> userToResponseTypeMap;

    @Autowired
    public UserConverter(ModelMapper modelMapper) {
        // Setup for UpdateUserConverter
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.userTypeMap = modelMapper.createTypeMap(UserDTO.class, User.class);
        this.userTypeMap.addMappings(mapper -> mapper.skip(User::setId));

        // Setup for UserResToUserConverter
        this.userToResponseTypeMap = modelMapper.createTypeMap(User.class, UserResponse.class);
        this.userToResponseTypeMap.addMappings(mapper -> {
            mapper.map(src -> src.getRole().getId(), UserResponse::setRoleId);
        });
    }

    public void updateEntity(UserDTO userDTO, User user) {
        this.userTypeMap.map(userDTO, user);
    }

    public UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        this.userToResponseTypeMap.map(user, userResponse);
        return userResponse;
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        this.userTypeMap.map(userDTO, user);
        return user;
    }
}
