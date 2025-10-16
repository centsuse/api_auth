package com.centsuse.api_auth.enums;

/**
 * 数据权限范围枚举
 * @author bobo
 */
public enum DataScopeType {
    ALL(1, "全部"),
    CURRENT_DEPT(2, "本部门"),
    CURRENT_AND_CHILD_DEPT(3, "本部门及以下"),
    SELF(4, "仅自己"),
    CUSTOM(5, "自定义");

    private final int code;
    private final String desc;

    DataScopeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
}

