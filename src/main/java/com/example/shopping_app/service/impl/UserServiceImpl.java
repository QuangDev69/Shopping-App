package com.example.shopping_app.service.impl;

import com.example.shopping_app.Exceptional.DataNotFoundException;
import com.example.shopping_app.Exceptional.PermissionDenyException;
import com.example.shopping_app.convert.UserConverter;
import com.example.shopping_app.dto.UserDTO;
import com.example.shopping_app.entity.Role;
import com.example.shopping_app.entity.User;
import com.example.shopping_app.middleware.JwtUtil;
import com.example.shopping_app.repository.RoleRepository;
import com.example.shopping_app.repository.UserRepository;
import com.example.shopping_app.response.UserResponse;
import com.example.shopping_app.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserConverter userConverter;


    @Override
    //Register User
    public User createUser(UserDTO userDTO) {
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Don't have Role..."));
        if(role.getName().equals(Role.ADMIN)){
            throw new PermissionDenyException("You can't register an account as an Admin!");
        }

        User newUser = userConverter.toEntity(userDTO);
        newUser.setRole(role);
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        userRepository.save(newUser); // Lưu User mới vào cơ sở dữ liệu
        return newUser;
    }

    @Override
    public String login(String phoneNumber, String password) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Invalid phone number / password");
        }
        User existingUser = optionalUser.get();
        if (existingUser.getFacebookAccountId() !=0 || existingUser.getGoogleAccountId() != 0) {
            return jwtUtil.generateToken(existingUser);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
            if (authentication.isAuthenticated()) {
                return jwtUtil.generateToken(existingUser);
            } else throw new BadCredentialsException("Wrong phone number or password!");
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Authentication failed", e);
        }
    }

    @Override
    public UserResponse getUserDetail(String token) {
        if(jwtUtil.isExpireToken(token)){
            throw new RuntimeException("Token is expire");
        }
        String phoneNumber = jwtUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if(user.isPresent()) {
            return userConverter.toResponse(user.get());
        }
        else throw new RuntimeException("User not found");
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long userId, UserDTO userDTO) {
        User existingUser = userRepository.findById(userId).orElse(null);

        String newPhone = userDTO.getPhoneNumber();
        assert existingUser != null;
        if(!existingUser.getPhoneNumber().equals(newPhone) && userRepository.existsByPhoneNumber(newPhone)){
            throw new DataIntegrityViolationException("Phone number is already exists");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found with id: " + userDTO.getRoleId()));

        if(role.getName().equals(Role.ADMIN)){
            throw new PermissionDenyException("You cannot update the account to an Admin role!");
        }

//        existingUser.setRole(role);
        userConverter.updateEntity(userDTO, existingUser);
        if (userDTO.getPassword() !=null && userDTO.getConfirmPassword() != null) {
            if(!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
                throw new DataIntegrityViolationException("Password does not match");
            }
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            existingUser.setPassword(encodedPassword);
        }

        userRepository.save(existingUser);

        return userConverter.toResponse(existingUser);
    }


}
