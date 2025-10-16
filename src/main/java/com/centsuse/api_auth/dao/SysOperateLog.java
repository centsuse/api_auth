package com.centsuse.api_auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * 操作日志表
 * @TableName sys_operate_log
 */
@TableName(value ="sys_operate_log")
public class SysOperateLog {
    /**
     * 日志ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作类型：1-新增，2-修改，3-删除，4-查询
     */
    private Integer type;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 操作人ID
     */
    private Long userId;

    /**
     * 操作人姓名
     */
    private String userName;

    /**
     * 操作IP
     */
    private String ipAddress;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作状态：0-失败，1-成功
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 日志ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 日志ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 应用编码
     */
    public String getAppCode() {
        return appCode;
    }

    /**
     * 应用编码
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    /**
     * 操作模块
     */
    public String getModule() {
        return module;
    }

    /**
     * 操作模块
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * 操作类型：1-新增，2-修改，3-删除，4-查询
     */
    public Integer getType() {
        return type;
    }

    /**
     * 操作类型：1-新增，2-修改，3-删除，4-查询
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 操作内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 操作内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 操作人ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 操作人ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 操作人姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 操作人姓名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 操作IP
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * 操作IP
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * 用户代理
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 用户代理
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * 操作时间
     */
    public Date getOperateTime() {
        return operateTime;
    }

    /**
     * 操作时间
     */
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * 操作状态：0-失败，1-成功
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 操作状态：0-失败，1-成功
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 错误信息
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 错误信息
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
        SysOperateLog other = (SysOperateLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAppCode() == null ? other.getAppCode() == null : this.getAppCode().equals(other.getAppCode()))
            && (this.getModule() == null ? other.getModule() == null : this.getModule().equals(other.getModule()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getIpAddress() == null ? other.getIpAddress() == null : this.getIpAddress().equals(other.getIpAddress()))
            && (this.getUserAgent() == null ? other.getUserAgent() == null : this.getUserAgent().equals(other.getUserAgent()))
            && (this.getOperateTime() == null ? other.getOperateTime() == null : this.getOperateTime().equals(other.getOperateTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getErrorMessage() == null ? other.getErrorMessage() == null : this.getErrorMessage().equals(other.getErrorMessage()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAppCode() == null) ? 0 : getAppCode().hashCode());
        result = prime * result + ((getModule() == null) ? 0 : getModule().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getIpAddress() == null) ? 0 : getIpAddress().hashCode());
        result = prime * result + ((getUserAgent() == null) ? 0 : getUserAgent().hashCode());
        result = prime * result + ((getOperateTime() == null) ? 0 : getOperateTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getErrorMessage() == null) ? 0 : getErrorMessage().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", appCode=").append(appCode);
        sb.append(", module=").append(module);
        sb.append(", type=").append(type);
        sb.append(", content=").append(content);
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", ipAddress=").append(ipAddress);
        sb.append(", userAgent=").append(userAgent);
        sb.append(", operateTime=").append(operateTime);
        sb.append(", status=").append(status);
        sb.append(", errorMessage=").append(errorMessage);
        sb.append("]");
        return sb.toString();
    }
}