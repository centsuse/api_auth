package com.centsuse.api_auth.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * @author bobo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefreshTokenRequest {
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;
}