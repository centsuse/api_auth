package com.centsuse.api_auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="sys_dept")
@Data
public class SysDept {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String appCode;

    private String name;

    private Long parentId;

    private String ancestors;

    private String leader;

    private String phone;

    private String email;

    private Integer sort;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Long createBy;

    private Long updateBy;
}