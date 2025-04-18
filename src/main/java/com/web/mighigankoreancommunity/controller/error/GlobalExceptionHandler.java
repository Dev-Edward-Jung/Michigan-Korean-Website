package com.web.mighigankoreancommunity.controller.error;


import com.web.mighigankoreancommunity.dto.ApiResponse;
import com.web.mighigankoreancommunity.error.UnauthorizedRestaurantAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedRestaurantAccessException.class)
    public ResponseEntity<?> unauthorizedRestaurantAccessException(UnauthorizedRestaurantAccessException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }

    // 예: 500 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail("Server Error.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
