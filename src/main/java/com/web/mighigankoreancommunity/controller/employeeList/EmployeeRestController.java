package com.web.mighigankoreancommunity.controller.employeeList;


import com.web.mighigankoreancommunity.dto.ApiResponse;
import com.web.mighigankoreancommunity.dto.EmployeeDTO;
import com.web.mighigankoreancommunity.entity.CustomUserDetails;
import com.web.mighigankoreancommunity.entity.Employee;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/employee")
@RestController
public class EmployeeRestController {
    private final EmployeeService employeeService;

    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<String>> sendInvitation(@RequestBody EmployeeDTO employeeDTO,
                                                              @AuthenticationPrincipal CustomUserDetails user) {
        String invitationLink = employeeService.sendInvitationEmail(employeeDTO, user.getOwner().getId());

        return ResponseEntity.ok(
                ApiResponse.success(invitationLink, "Invitation sent successfully")
        );
    }

    @GetMapping("/list")
    public List<EmployeeDTO> getEmployees(@RequestParam Long restaurantId,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<EmployeeDTO> employeeDTOList = employeeService.getAllEmployees(restaurantId, customUserDetails.getOwner());
        return employeeDTOList;


    }
}
