package com.centsuse.api_auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * 令牌管理表
 * @TableName sys_token
 */
@TableName(value ="sys_token")
public class SysToken {
    /**
     * 令牌ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * 过期时间(秒)
     */
    private Long expiresIn;

    /**
     * 刷新令牌过期时间(秒)
     */
    private Long refreshExpiresIn;

    /**
     * 权限范围
     */
    private String scope;

    /**
     * 签发时间
     */
    private Date issueTime;

    /**
     * 状态：0-失效，1-有效
     */
    private Integer status;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 令牌ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 令牌ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * 访问令牌
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 访问令牌
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 刷新令牌
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * 刷新令牌
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * 令牌类型
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * 令牌类型
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * 过期时间(秒)
     */
    public Long getExpiresIn() {
        return expiresIn;
    }

    /**
     * 过期时间(秒)
     */
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * 刷新令牌过期时间(秒)
     */
    public Long getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    /**
     * 刷新令牌过期时间(秒)
     */
    public void setRefreshExpiresIn(Long refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }

    /**
     * 权限范围
     */
    public String getScope() {
        return scope;
    }

    /**
     * 权限范围
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * 签发时间
     */
    public Date getIssueTime() {
        return issueTime;
    }

    /**
     * 签发时间
     */
    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    /**
     * 状态：0-失效，1-有效
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态：0-失效，1-有效
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 客户端ID
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 客户端ID
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
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
        SysToken other = (SysToken) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getAppCode() == null ? other.getAppCode() == null : this.getAppCode().equals(other.getAppCode()))
            && (this.getAccessToken() == null ? other.getAccessToken() == null : this.getAccessToken().equals(other.getAccessToken()))
            && (this.getRefreshToken() == null ? other.getRefreshToken() == null : this.getRefreshToken().equals(other.getRefreshToken()))
            && (this.getTokenType() == null ? other.getTokenType() == null : this.getTokenType().equals(other.getTokenType()))
            && (this.getExpiresIn() == null ? other.getExpiresIn() == null : this.getExpiresIn().equals(other.getExpiresIn()))
            && (this.getRefreshExpiresIn() == null ? other.getRefreshExpiresIn() == null : this.getRefreshExpiresIn().equals(other.getRefreshExpiresIn()))
            && (this.getScope() == null ? other.getScope() == null : this.getScope().equals(other.getScope()))
            && (this.getIssueTime() == null ? other.getIssueTime() == null : this.getIssueTime().equals(other.getIssueTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getClientId() == null ? other.getClientId() == null : this.getClientId().equals(other.getClientId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAppCode() == null) ? 0 : getAppCode().hashCode());
        result = prime * result + ((getAccessToken() == null) ? 0 : getAccessToken().hashCode());
        result = prime * result + ((getRefreshToken() == null) ? 0 : getRefreshToken().hashCode());
        result = prime * result + ((getTokenType() == null) ? 0 : getTokenType().hashCode());
        result = prime * result + ((getExpiresIn() == null) ? 0 : getExpiresIn().hashCode());
        result = prime * result + ((getRefreshExpiresIn() == null) ? 0 : getRefreshExpiresIn().hashCode());
        result = prime * result + ((getScope() == null) ? 0 : getScope().hashCode());
        result = prime * result + ((getIssueTime() == null) ? 0 : getIssueTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getClientId() == null) ? 0 : getClientId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", appCode=").append(appCode);
        sb.append(", accessToken=").append(accessToken);
        sb.append(", refreshToken=").append(refreshToken);
        sb.append(", tokenType=").append(tokenType);
        sb.append(", expiresIn=").append(expiresIn);
        sb.append(", refreshExpiresIn=").append(refreshExpiresIn);
        sb.append(", scope=").append(scope);
        sb.append(", issueTime=").append(issueTime);
        sb.append(", status=").append(status);
        sb.append(", clientId=").append(clientId);
        sb.append("]");
        return sb.toString();
    }
}