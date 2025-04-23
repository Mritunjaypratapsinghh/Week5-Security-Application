package com.week5.SecurityApp.SecurityApplication.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The type Api response.
 *
 * @param <T> the type parameter
 */
@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private LocalDateTime timestamp;
    private T data;
    private ApiError apiError;

    /**
     * Instantiates a new Api response.
     */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Instantiates a new Api response.
     *
     * @param data the data
     */
    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    /**
     * Instantiates a new Api response.
     *
     * @param apiError the api error
     */
    public ApiResponse(ApiError apiError) {
        this();
        this.apiError = apiError;
    }


}
