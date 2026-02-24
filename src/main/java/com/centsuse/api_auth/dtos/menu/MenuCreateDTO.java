package com.centsuse.api_auth.dtos.menu;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author bobo
 */
@Data
public class MenuCreateDTO {
    @NotBlank(message = "应用编码不能为空")
    private String appCode;

    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @NotBlank(message = "权限编码不能为空")
    private String code;

    private Long parentId;

    private String path;

    private String component;

    private String icon;

    private Integer sort;

    private Integer status;

    private String description;
}
