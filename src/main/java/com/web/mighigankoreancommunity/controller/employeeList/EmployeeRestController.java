package com.web.mighigankoreancommunity.controller.employeeList;


import com.web.mighigankoreancommunity.dto.EmployeeDTO;
import com.web.mighigankoreancommunity.entity.CustomUserDetails;
import com.web.mighigankoreancommunity.entity.Employee;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/employee")
@RestController
public class EmployeeRestController {
    private final EmployeeService employeeService;

    @PostMapping("/invite")
    public void sendInvitation(@RequestBody EmployeeDTO employeeDTO, @AuthenticationPrincipal CustomUserDetails user) {
        employeeService.sendInvitationEmail(employeeDTO, user.getOwner().getId());
    }
}
