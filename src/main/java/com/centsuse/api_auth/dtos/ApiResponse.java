package com.centsuse.api_auth.dtos;

import lombok.*;

/**
 * @author bobo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("成功")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static ApiResponse<?> error(String message) {
        return ApiResponse.builder()
                .code(500)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static ApiResponse<?> error(Integer code, String message) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}