package com.centsuse.api_auth.dtos.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionUpdateDTO {
    @NotNull(message = "权限ID不能为空")
    private Long permissionId;

    @NotBlank(message = "应用编码不能为空")
    private String appCode;

    private String name;

    private String code;

    private Integer type;

    private String path;

    private String component;

    private String method;

    private String icon;

    private Integer sort;

    private Integer status;

    private String description;
}
