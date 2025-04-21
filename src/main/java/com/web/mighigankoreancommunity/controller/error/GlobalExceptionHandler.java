package com.web.mighigankoreancommunity.controller.error;


import com.web.mighigankoreancommunity.dto.ApiResponse;
import com.web.mighigankoreancommunity.error.UnauthorizedRestaurantAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        // 로그 남기기
        ex.printStackTrace();
        // 원하는 페이지로 이동 (HTML 뷰 이름)
        return "error/custom-error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException ex) {
        return "error/404-error";
    }

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointer(NullPointerException ex) {
        return "error/403-error";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFound(NoResourceFoundException ex) {
        return "error/custom-error"; // 또는 다른 에러 페이지
    }
}
