package com.centsuse.api_auth.utils;

public class AppCodeHolder {
    
    private static final ThreadLocal<String> APP_CODE = new ThreadLocal<>();

    public static void setAppCode(String appCode) {
        APP_CODE.set(appCode);
    }

    public static String getAppCode() {
        return APP_CODE.get();
    }

    public static void clear() {
        APP_CODE.remove();
    }

    public static String getAppCodeOrDefault() {
        String appCode = APP_CODE.get();
        return appCode != null ? appCode : "default";
    }
}
