package com.beyondsales.beyondsales.security.payload.response;

import java.time.Instant;

public record ApiResponse<T>(String status, String code, String message, T payload, Instant timestamp) {

    public static <T> ApiResponse<T> success(T payload) {
        return new ApiResponse<>("OK", "SUCCESS", null, payload, Instant.now());
    }

    public static <T> ApiResponse<T> successMessage(String message) {
        return new ApiResponse<>("OK", "SUCCESS", message, null, Instant.now());
    }

    public static <T> ApiResponse<T> failure(String code, String message) {
        return new ApiResponse<>("ERROR", code, message, null, Instant.now());
    }
}
