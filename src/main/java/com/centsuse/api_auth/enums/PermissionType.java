package com.centsuse.api_auth.enums;

/**
 * 权限类型枚举
 * @author bobo
 */
public enum PermissionType {
    MENU(1, "菜单"),
    BUTTON(2, "按钮"),
    API(3, "接口"),
    DATA(4, "数据");

    private final int code;
    private final String desc;

    PermissionType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
}
