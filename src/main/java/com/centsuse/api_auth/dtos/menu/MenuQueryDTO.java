package com.centsuse.api_auth.dtos.menu;

import lombok.Data;

@Data
public class MenuQueryDTO {
    private String appCode;

    private Integer pageNum;

    private Integer pageSize;
}
