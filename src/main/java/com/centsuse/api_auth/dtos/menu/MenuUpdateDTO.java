package com.centsuse.api_auth.dtos.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuUpdateDTO {
    @NotNull(message = "菜单ID不能为空")
    private Long menuId;

    @NotBlank(message = "应用编码不能为空")
    private String appCode;

    private String name;

    @NotBlank(message = "权限编码不能为空")
    private String code;

    private String path;

    private String component;

    private String icon;

    private Integer sort;

    private Integer status;

    private String description;
}
