package com.example.main.service;

import com.example.main.entity.User;
import com.example.main.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
          String.format("Пользователь с таким '%s' не найден", username)
      ));
      return new org.springframework.security.core.userdetails.User(
          user.getEmail(),
          user.getPassword(),
          Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
      );
  }

  public User createUser(User user) {
    user.setRole("ROLE_USER");
    userRepository.save(user);
    return user;
  }
}
