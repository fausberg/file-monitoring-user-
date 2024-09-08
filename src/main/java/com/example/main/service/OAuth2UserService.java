package com.example.main.service;

import static com.example.main.enums.Role.ROLE_USER;

import com.example.main.dto.UserPrincipal;
import com.example.main.dto.request.UserRequestForRegistration;
import com.example.main.entity.User;
import com.example.main.enums.Role;
import com.example.main.repository.UserRepository;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  @SneakyThrows
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
    log.trace("Load user {}", oAuth2UserRequest);
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    return processOAuth2User(oAuth2UserRequest, oAuth2User);
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    UserRequestForRegistration userInfoDto = UserRequestForRegistration
        .builder()
        .name(oAuth2User.getAttributes().get("name").toString())
        .email(oAuth2User.getAttributes().get("email").toString())
        .password(String.valueOf(Math.random()))
        .build();

    Optional<User> userOptional = userRepository.findByEmail(userInfoDto.getEmail());
    User user = userOptional
        .map(existingUser -> updateExistingUser(existingUser, userInfoDto))
        .orElseGet(() -> registerNewUser(oAuth2UserRequest, userInfoDto));
    return UserPrincipal.create(user, oAuth2User.getAttributes());
  }

  private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, UserRequestForRegistration userInfoDto) {
    User user = new User();
    user.setName(userInfoDto.getName());
    user.setEmail(userInfoDto.getEmail());
    user.setPassword(userInfoDto.getPassword());
    user.setRoleEnum(ROLE_USER);
    return userRepository.save(user);

  }

  private User updateExistingUser(User existingUser, UserRequestForRegistration userInfoDto) {
    existingUser.setName(userInfoDto.getName());
    existingUser.setEmail(userInfoDto.getEmail());
    existingUser.setPassword(userInfoDto.getPassword());
    existingUser.setRoleEnum(ROLE_USER);
    return userRepository.save(existingUser);
  }

}
