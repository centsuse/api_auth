package com.centsuse.api_auth.dtos.sysuser;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserQueryDTO {
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private Integer status;
    private Long deptId;
    private Integer pageNum;
    private Integer pageSize;
}
