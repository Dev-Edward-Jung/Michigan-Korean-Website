package com.web.mighigankoreancommunity.controller.employeeList;


import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/page/employee")
@Controller
public class EmployeeController {
    private final EmployeeService employeeService;


    @GetMapping("/list")
    public String employeeList() {
        return "restaurant/employee-list";
    }


    @GetMapping("/login")
    public String employeeLogin(@AuthenticationPrincipal CustomUserDetails loginUser){
        if (loginUser != null) {
            return "redirect:/page/schedule/list";
        }
        return "user/employee-login";
    }





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
        System.out.println(password + " --------------------------------------- ");
        boolean isExpired = employeeService.isInvitationExpired(token);
        if (isExpired) {
            return "error/expired-error";
        }
//        save employee password
        employeeService.registerEmployee(token, password);
        return "user/employee-login";
    }
}
