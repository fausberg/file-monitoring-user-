package com.example.main.enums;

public enum Role {
  ROLE_USER("USER"),
  ROLE_ADMIN("ADMIN");

  private final String text;

  Role(String text) {
    this.text = text;
  }
  public String text() {
    return text;
  }
}
