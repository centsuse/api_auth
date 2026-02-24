package com.centsuse.api_auth.dtos.sysuser;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateUpdateDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    private String email;

    private String phone;

    private String avatar;

    @NotBlank(message = "状态不能为空")
    private String status;

    private Long[] roleIds;
}
