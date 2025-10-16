package com.centsuse.api_auth.enums;

/**
 * 操作类型枚举
 * @author bobo
 */
public enum OperateType {
    CREATE(1, "新增"),
    UPDATE(2, "修改"),
    DELETE(3, "删除"),
    QUERY(4, "查询");

    private final int code;
    private final String desc;

    OperateType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
}