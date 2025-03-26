package com.web.mighigankoreancommunity.controller.member;


import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/page/member")
@Controller
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage() {
        return "user/auth-login";
    }

//    @PostMapping("/login")
//    public String login(Member member) {
//        System.out.println(member.getMemberEmail());
//
////        login service logic
//        memberService.login(member);
//
//        return "restaurant/inventory-list";
//    }
//    Page Move
    @GetMapping("/register")
    public String RegisterPage() {return "user/auth-register";}



//    Save Info
    @PostMapping("/register")
    public String registerMember(Member member) {
        System.out.println(member.getMemberName());
        memberService.saveMember(member);
        return "user/auth-login";
    }

    @GetMapping("/forgot/password")
    public String forgotPasswordPage() {return "user/auth-forgot-password";}
}
