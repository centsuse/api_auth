package com.centsuse.api_auth.utils;

import java.util.regex.Pattern;

/**
 * 密码验证工具类
 */
public class PasswordValidator {

    // 密码强度正则表达式：至少8位，包含大小写字母、数字和特殊字符
    private static final String PASSWORD_PATTERN = 
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    /**
     * 验证密码强度
     * @param password 密码
     * @return 是否符合密码强度要求
     */
    public static boolean validatePassword(String password) {
        if (password == null) {
            return false;
        }
        return pattern.matcher(password).matches();
    }

    /**
     * 获取密码强度提示信息
     * @return 密码强度要求提示
     */
    public static String getPasswordStrengthHint() {
        return "密码长度至少8位，包含大小写字母、数字和特殊字符";
    }
}
