package com.jjery.springcrud.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
  private String loginId;
  private String nickname;
  private String refreshToken; // Refresh Token 추가
  private String userRole;
}
