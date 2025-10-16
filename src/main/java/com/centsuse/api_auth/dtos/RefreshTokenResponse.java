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
public class RefreshTokenResponse {

    private String accessToken;

    private Long expiresIn;

}