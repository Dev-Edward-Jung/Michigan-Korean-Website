package com.web.mighigankoreancommunity.controller.employeeList;


import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/page/employee")
@Controller
@Tag(name = "EmployeePageLoader", description = "Return All the pages related to employee")
public class EmployeeController {
    private final EmployeeService employeeService;


    @GetMapping("/list")
    public String employeeList(Model model, @AuthenticationPrincipal CustomUserDetails loginUser) {
        model.addAttribute("isOwner", loginUser.isOwner());
        model.addAttribute("isEmployee", loginUser.isEmployee());

        if (loginUser.isEmployee()) {
            model.addAttribute("memberRole", loginUser.getCurrentMemberRole());
        }
        return "owner-pages/employee-list";
    }


    @GetMapping("/login")
    public String employeeLogin(@AuthenticationPrincipal CustomUserDetails loginUser){
        if (loginUser != null) {
            return "redirect:/page/schedule/list";
        }
        return "user/employee-login";
    }

    @GetMapping("/forgot/password")
    public String forgotPasswordPage() {return "user/employee-forgot-password";}





//    When an owner sent a invitation to employee
    @GetMapping("/invited")
    public String invited(@RequestParam("token") String token) {
        boolean isExpired = employeeService.isInvitationExpired(token);
        if (isExpired) {
            return "error/expired-error";
        }
        employeeService.getInvitationInfoByToken(token);
        return "user/employee-register";
    }

    @PostMapping("/register")
    public String registerEmployee(@RequestParam("token") String token,@RequestParam("restaurantId") Long restaurantId,  String password) {
        boolean isExpired = employeeService.isInvitationExpired(token);
        if (isExpired) {
            return "error/expired-error";
        }
//        save employee password
        employeeService.registerEmployee(token, password);
        return "user/employee-login";
    }


    @GetMapping("/reset/password")
    public String resetPassword(@RequestParam("token") String token) {
        boolean isExpired = employeeService.isPasswordExpired(token);
        if (isExpired) {
            return "error/expired-error";
        } else{
            return "user/employee-reset-password";
        }
    }


    @PostMapping("/reset/password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, String password) {
        employeeService.resetPassword(token, password);
        return new ResponseEntity(HttpStatus.OK);
    }


}
