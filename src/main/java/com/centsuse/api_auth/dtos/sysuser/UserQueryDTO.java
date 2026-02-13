package com.centsuse.api_auth.dtos.sysuser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bobo
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserQueryDTO {
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private Integer status;
    private String appCode;
    private Long deptId;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}