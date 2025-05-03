package com.web.mighigankoreancommunity.controller.owner;


import com.web.mighigankoreancommunity.dto.OwnerDTO;
import com.web.mighigankoreancommunity.dto.PasswordRequest;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import com.web.mighigankoreancommunity.service.owner.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/owner")
@RestController
public class OwnerRestController {
    private final OwnerService ownerService;

    public String emailToLowerCase(String email){
        return email.trim().toLowerCase();

    }

    @PostMapping("/checkEmail")
    public boolean checkEmail(@RequestBody String email) {
        System.out.println(ownerService.getMemberByEmail(email));
        return ownerService.getMemberByEmail(email);
    }

    @GetMapping("/me")
    public ResponseEntity<OwnerDTO> getCurrentUser(@AuthenticationPrincipal Owner owner) {
        System.out.println(owner.getOwnerName());
        if (owner == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        OwnerDTO dto = ownerService.memberToDTO(owner);

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/forgot/password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        email = emailToLowerCase(email);
        if (ownerService.ownerForgotPasswordService(email)) {
            System.out.println("Owner mail sent!");
            return ResponseEntity.ok("Owner password reset email sent.");
        } else {
            System.out.println("Email does not exist!");
            return ResponseEntity.badRequest().body("Email does not exist in our system.");
        }
    }

    @PostMapping("/reset/password")
    public ResponseEntity<String> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("email") String email,
            @RequestBody PasswordRequest request) {
        String password = request.getPassword();
        email = email.toLowerCase();

        // ✅ 1. 토큰 만료 여부 확인
        if (ownerService.isPasswordTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired. Please request a new reset email.");
        }

        // ✅ 2. 이메일과 토큰이 일치하는지 확인
        if (!ownerService.isTokenValidForEmail(token, email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token or email.");
        }

        // ✅ 3. 비밀번호 재설정
        ownerService.resetPassword(email, password);

        return ResponseEntity.ok("Password reset successful.");
    }
}
