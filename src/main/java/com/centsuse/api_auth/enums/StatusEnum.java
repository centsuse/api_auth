package com.centsuse.api_auth.enums;

/**
 * 状态枚举
 * @author bobo
 */
public enum StatusEnum {
    DISABLED(0, "禁用"),
    ENABLED(1, "正常");

    private final int code;
    private final String desc;

    StatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
}