package com.centsuse.api_auth.configs.constants;

/**
 * @author bobo
 */
public class RedisKeyConstants {
    public static final String TOKEN_BLACKLIST = "blacklist:";
    public static final String USER_PERMISSIONS = "user:permissions:";
    public static final String USER_ROLES = "user:roles:";
    public static final String API_RATE_LIMIT = "rate:limit:";

    public static String getUserPermissionsKey(Long userId) {
        return USER_PERMISSIONS + userId;
    }
}