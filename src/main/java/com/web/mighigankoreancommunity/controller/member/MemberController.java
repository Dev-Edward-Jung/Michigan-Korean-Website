package com.web.mighigankoreancommunity.controller.member;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/page/member")
@Controller
public class MemberController {
    @GetMapping("/login")
    public String loginPage() {
        return "user/auth-login";
    }
}
