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
public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String nickname;
    private String email;
    private String phone;
    private String appCode;
}