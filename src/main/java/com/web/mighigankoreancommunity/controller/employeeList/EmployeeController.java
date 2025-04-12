package com.web.mighigankoreancommunity.controller.employeeList;


import ch.qos.logback.core.model.Model;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public String registerEmployee(@RequestParam String token, String password) {
        return "redirect:/page/announcement/list";
    }
}
