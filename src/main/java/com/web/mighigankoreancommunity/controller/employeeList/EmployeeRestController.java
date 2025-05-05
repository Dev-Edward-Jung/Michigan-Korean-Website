package com.web.mighigankoreancommunity.controller.employeeList;


import com.web.mighigankoreancommunity.dto.ApiResponse;
import com.web.mighigankoreancommunity.dto.EmployeeDTO;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@Tag(name = "Employee", description = "APIs for employee management and password recovery")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    public String emailToLowerCase(String email){
        return email.trim().toLowerCase();
    }

    @Operation(summary = "Send employee invitation", description = "Sends an invitation email to an employee for a specific restaurant.")
    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<String>> sendInvitation(
            @RequestBody EmployeeDTO employeeDTO,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        String invitationLink = employeeService.sendInvitationEmail(employeeDTO, user.getOwner().getId());
        return ResponseEntity.ok(
                ApiResponse.success(invitationLink, "Invitation sent successfully")
        );
    }

    @Operation(summary = "Get employee list", description = "Retrieves a list of employees for a given restaurant.")
    @GetMapping("/list")
    public List<EmployeeDTO> getEmployees(
            @Parameter(description = "Restaurant ID") @RequestParam Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return employeeService.getAllEmployees(restaurantId, customUserDetails.getOwner());
    }

    @Operation(summary = "Send password reset email", description = "Sends a password reset email to the given employee email.")
    @PostMapping("/forgot/password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody @Parameter(description = "Employee email") String email
    ) {
        email = emailToLowerCase(email);
        if (employeeService.employeeForgotPasswordService(email)) {
            return ResponseEntity.ok("Employee password reset email sent.");
        } else {
            return ResponseEntity.badRequest().body("Email does not exist in our system.");
        }
    }

    @Operation(summary = "Reset employee password", description = "Resets the employee password using a valid token and email.")
    @PostMapping("/reset/password")
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "Reset token") @RequestParam("token") String token,
            @Parameter(description = "Employee email") @RequestParam("email") String email,
            @RequestBody @Parameter(description = "New password") String password
    ) {
        email = email.toLowerCase();

        if (employeeService.isPasswordExpired(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired. Please request a new reset email.");
        }

        if (!employeeService.isTokenValidForEmail(token, email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token or email.");
        }

        employeeService.resetPassword(email, password);
        return ResponseEntity.ok("Password reset successful.");
    }
}

