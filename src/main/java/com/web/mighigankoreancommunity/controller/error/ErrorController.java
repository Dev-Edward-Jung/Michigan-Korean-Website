package com.web.mighigankoreancommunity.controller.error;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@RequestMapping("page/error")
public class ErrorController {
    @GetMapping("/400")
    public String error400() {
        return "error/400error";
    }
}
