package com.web.mighigankoreancommunity.controller.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        System.out.println("에러 발생!");
        System.out.println("status code = " + status);
        System.out.println("uri = " + request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

        System.out.println("❗ error status: " + status);
        System.out.println("❗ request URI: " + uri);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == 404) {
                return "error/404-error";
            } else if (statusCode == 403) {
                return "error/403-error";
            } else if (statusCode == 500) {
                return "error/500-error";
            }
        }
        return "redirect:/page/owner/login";
    }
}
