package com.example.main.mapper;

import com.example.main.dto.request.UserRequestForRegistration;
import com.example.main.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final PasswordEncoder passwordEncoder;

  public User userRequestForRegistrationToUser(UserRequestForRegistration userRequestForRegistration) {
    return User.builder()
        .email(userRequestForRegistration.getEmail())
        .password(passwordEncoder.encode(userRequestForRegistration.getPassword()))
        .name(userRequestForRegistration.getName())
        .build();
  }
}
