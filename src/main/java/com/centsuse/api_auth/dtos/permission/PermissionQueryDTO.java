package com.centsuse.api_auth.dtos.permission;

import lombok.Data;

@Data
public class PermissionQueryDTO {
    private String appCode;

    private String name;

    private Integer type;

    private Integer status;

    private Integer pageNum;

    private Integer pageSize;
}
