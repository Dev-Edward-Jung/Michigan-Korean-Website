package com.web.mighigankoreancommunity.controller.owner;


import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import com.web.mighigankoreancommunity.service.owner.OwnerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/page/owner")
@Controller
public class OwnerController {
    private final OwnerService ownerService;



    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal CustomUserDetails loginUser) {
        if (loginUser != null) {
            return "redirect:/page/restaurant/list";
        }
        return "user/owner-login";
    }

//    Page Move
    @GetMapping("/register")
    public String RegisterPage() {return "user/owner-register";}



//    Save Info
    @PostMapping("/register")
    public String registerMember(Owner owner) {
        System.out.println(owner.getOwnerName());
        ownerService.saveOwner(owner);
        return "user/owner-login";
    }

    @GetMapping("/forgot/password")
    public String forgotPasswordPage() {return "/user/owner-forgot-password";}


    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.logout(); // Java EE 방식 로그아웃
        return "redirect:/page/owner/login?logout=true"; // 로그아웃 후 리디렉션
    }


    @GetMapping("/forgot/password")
    public String resetPassword(@RequestParam("token") String token) {
        boolean isExpired = ownerService.isPasswordTokenExpired(token);
        if (isExpired) {
            return "error/expired-error";
        } else{
            return "user/owner-reset-password";
        }
    }

}
