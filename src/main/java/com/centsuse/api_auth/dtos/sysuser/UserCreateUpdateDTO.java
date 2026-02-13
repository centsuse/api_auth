package com.centsuse.api_auth.dtos.sysuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author bobo
 */
@Data
public class UserCreateUpdateDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    //创建时必传，更新时选填
    private String password;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    private String email;
    private String phone;
    private String avatar;

    @NotNull(message = "部门ID不能为空")
    private Long deptId;

    @NotNull(message = "应用编码不能为空")
    private String appCode;

    @NotNull(message = "状态不能为空")
    private Integer status;

    // 用户角色ID数组
    private Long[] roleIds;
}