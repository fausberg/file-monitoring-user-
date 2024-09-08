package com.example.main.controller;

import com.example.main.entity.User;
import com.example.main.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/create")
  public void createUser(@RequestBody User user) {
    userService.createUser(user);
  }

  @GetMapping("/info")
  public String getInfo() {
    return "Hello";
  }

}
