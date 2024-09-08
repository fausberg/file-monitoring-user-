package com.example.main.controller;

import static org.springframework.http.HttpStatus.OK;

import com.example.main.dto.request.JwtRequest;
import com.example.main.dto.request.UserRequestForRegistration;
import com.example.main.dto.response.JwtResponse;
import com.example.main.entity.User;
import com.example.main.exceptions.AppError;
import com.example.main.mapper.UserMapper;
import com.example.main.service.UserService;
import com.example.main.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  private final JwtTokenUtils tokenUtils;

  private final AuthenticationManager authenticationManager;

  private final UserMapper userMapper;

  @PostMapping("/auth")
  public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest request) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    } catch (BadCredentialsException e ) {
      return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неверный логин или пароль"), HttpStatus.UNAUTHORIZED);
    }
    UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
    String token = tokenUtils.generateToken(userDetails);
    return ResponseEntity.ok(new JwtResponse(token));
  }

  @PostMapping("/registration")
  public ResponseEntity<?> createNewUser(@RequestBody UserRequestForRegistration userRequestForRegistration) {
    if(userService.userWithEmailIsExist(userRequestForRegistration.getEmail())) {
      return new ResponseEntity<>(
          new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с такой почтой уже существует"),
          HttpStatus.BAD_REQUEST);
    }

    User user = userMapper.userRequestForRegistrationToUser(userRequestForRegistration);
    userService.createUser(user);
    UserDetails userDetails = userService.loadUserByUsername(userRequestForRegistration.getEmail());
    String token = tokenUtils.generateToken(userDetails);
    return ResponseEntity.ok(new JwtResponse(token));
  }
}
