package com.centsuse.api_auth.dtos.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleQueryDTO {
    private String appCode;

    private String name;

    private Integer status;

    private Integer pageNum;

    private Integer pageSize;
}
