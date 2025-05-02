package com.web.mighigankoreancommunity.controller.employeeList;


import com.web.mighigankoreancommunity.dto.ApiResponse;
import com.web.mighigankoreancommunity.dto.EmployeeDTO;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/employee")
@RestController
public class EmployeeRestController {
    private final EmployeeService employeeService;

    public String emailToLowerCase(String email){
        return email.trim().toLowerCase();

    }

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

//    Sending Email
    @PostMapping("/forgot/password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        email = emailToLowerCase(email);
        if (employeeService.employeeForgotPasswordService(email)) {
            System.out.println("Employee mail sent!");
            return ResponseEntity.ok("Employee password reset email sent.");
        } else {
            System.out.println("Email does not exist!");
            return ResponseEntity.badRequest().body("Email does not exist in our system.");
        }
    }

//  After sending email, when user push reset button
@PostMapping("/reset/password")
public ResponseEntity<String> resetPassword(
        @RequestParam("token") String token,
        @RequestParam("email") String email,
        String password) {

    email = email.toLowerCase();

    // ✅ 1. 토큰 만료 여부 확인
    if (employeeService.isPasswordExpired(token)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired. Please request a new reset email.");
    }

    // ✅ 2. 이메일과 토큰이 일치하는지 확인
    if (!employeeService.isTokenValidForEmail(token, email)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token or email.");
    }

    // ✅ 3. 비밀번호 재설정
    employeeService.resetPassword(email, password);

    return ResponseEntity.ok("Password reset successful.");
}

}
