package com.web.mighigankoreancommunity.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApiResponse<T> {
    public boolean success;
    public T data;
    public String message;
    public int status;


    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, 200);
    }

    public static <T> ApiResponse<T> fail(String message, int status) {
        return new ApiResponse<>(false, null, message, status);
    }

}
