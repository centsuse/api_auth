package com.centsuse.api_auth.dtos;

import lombok.*;

/**
 * @author bobo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginResponse {
    private String accessToken;

    private String refreshToken;

    private Long expiresIn;

    private String tokenType;

    private UserInfo userInfo;
}