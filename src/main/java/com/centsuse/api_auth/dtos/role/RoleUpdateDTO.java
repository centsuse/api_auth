package com.centsuse.api_auth.dtos.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleUpdateDTO {
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @NotBlank(message = "应用编码不能为空")
    private String appCode;

    private String name;

    @NotBlank(message = "角色编码不能为空")
    private String code;

    private Integer dataScope;

    private Integer sort;

    private Integer status;

    private String description;
}
