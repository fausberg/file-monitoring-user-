package com.example.main.dto;

import com.example.main.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class UserPrincipal implements OAuth2User {

  private final User user;
  private final Map<String, Object> attributes;
  
  public UserPrincipal(User user, Map<String, Object> attributes) {
    this.user = user;
    this.attributes = attributes;
  }

  public static UserPrincipal create(User user, Map<String, Object> attributes) {
    return new UserPrincipal(user, attributes);
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Вернуть роли или другие GrantedAuthority
    return null;
  }

  @Override
  public String getName() {
    return user.getEmail(); // Уникальный идентификатор пользователя
  }

  public User getUser() {
    return user;
  }
}
