package com.example.main.dto.request;

import static com.example.main.validation.ValidationPattern.MAIL_PATTERN;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestForRegistration {

  @Email(regexp = MAIL_PATTERN)
  @NotNull
  private String email;

  @NotNull
  private String password;

  @NotNull
  private String name;
}
