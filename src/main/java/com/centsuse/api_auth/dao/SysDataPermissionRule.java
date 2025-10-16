package com.centsuse.api_auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * 数据权限规则表
 * @TableName sys_data_permission_rule
 */
@TableName(value ="sys_data_permission_rule")
public class SysDataPermissionRule {
    /**
     * 规则ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 规则类型：1-部门权限，2-自定义SQL，3-字段过滤
     */
    private Integer type;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限ID
     */
    private Long permissionId;

    /**
     * 条件表达式
     */
    private Object conditionExpression;

    /**
     * 自定义SQL
     */
    private String customSql;

    /**
     * 字段过滤规则
     */
    private Object fieldFilters;

    /**
     * 状态：0-禁用，1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 规则ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 规则ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 规则名称
     */
    public String getName() {
        return name;
    }

    /**
     * 规则名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 规则类型：1-部门权限，2-自定义SQL，3-字段过滤
     */
    public Integer getType() {
        return type;
    }

    /**
     * 规则类型：1-部门权限，2-自定义SQL，3-字段过滤
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 角色ID
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 角色ID
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 权限ID
     */
    public Long getPermissionId() {
        return permissionId;
    }

    /**
     * 权限ID
     */
    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    /**
     * 条件表达式
     */
    public Object getConditionExpression() {
        return conditionExpression;
    }

    /**
     * 条件表达式
     */
    public void setConditionExpression(Object conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    /**
     * 自定义SQL
     */
    public String getCustomSql() {
        return customSql;
    }

    /**
     * 自定义SQL
     */
    public void setCustomSql(String customSql) {
        this.customSql = customSql;
    }

    /**
     * 字段过滤规则
     */
    public Object getFieldFilters() {
        return fieldFilters;
    }

    /**
     * 字段过滤规则
     */
    public void setFieldFilters(Object fieldFilters) {
        this.fieldFilters = fieldFilters;
    }

    /**
     * 状态：0-禁用，1-正常
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态：0-禁用，1-正常
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 创建人
     */
    public Long getCreateBy() {
        return createBy;
    }

    /**
     * 创建人
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    /**
     * 更新人
     */
    public Long getUpdateBy() {
        return updateBy;
    }

    /**
     * 更新人
     */
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysDataPermissionRule other = (SysDataPermissionRule) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getRoleId() == null ? other.getRoleId() == null : this.getRoleId().equals(other.getRoleId()))
            && (this.getPermissionId() == null ? other.getPermissionId() == null : this.getPermissionId().equals(other.getPermissionId()))
            && (this.getConditionExpression() == null ? other.getConditionExpression() == null : this.getConditionExpression().equals(other.getConditionExpression()))
            && (this.getCustomSql() == null ? other.getCustomSql() == null : this.getCustomSql().equals(other.getCustomSql()))
            && (this.getFieldFilters() == null ? other.getFieldFilters() == null : this.getFieldFilters().equals(other.getFieldFilters()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        result = prime * result + ((getPermissionId() == null) ? 0 : getPermissionId().hashCode());
        result = prime * result + ((getConditionExpression() == null) ? 0 : getConditionExpression().hashCode());
        result = prime * result + ((getCustomSql() == null) ? 0 : getCustomSql().hashCode());
        result = prime * result + ((getFieldFilters() == null) ? 0 : getFieldFilters().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", roleId=").append(roleId);
        sb.append(", permissionId=").append(permissionId);
        sb.append(", conditionExpression=").append(conditionExpression);
        sb.append(", customSql=").append(customSql);
        sb.append(", fieldFilters=").append(fieldFilters);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}