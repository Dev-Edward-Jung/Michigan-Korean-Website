package com.web.mighigankoreancommunity.controller.member;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/page/member")
@Controller
public class MemberController {
    @GetMapping("/login")
    public String loginPage() {
        return "user/auth-login";
    }

    @GetMapping("/register")
    public String RegisterPage() {return "user/auth-register";}

    @GetMapping("/forgot/password")
    public String forgotPasswordPage() {return "user/auth-forgot-password";}
}
