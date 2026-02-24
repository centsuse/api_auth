package com.centsuse.api_auth.dtos.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionCreateDTO {
    @NotNull(message = "应用编码不能为空")
    private String appCode;

    private Long parentId;

    @NotBlank(message = "权限名称不能为空")
    private String name;

    @NotBlank(message = "权限编码不能为空")
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
