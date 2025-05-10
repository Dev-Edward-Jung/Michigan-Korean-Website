package com.web.mighigankoreancommunity.controller.owner;


import com.web.mighigankoreancommunity.dto.LoginRequest;
import com.web.mighigankoreancommunity.dto.OwnerDTO;
import com.web.mighigankoreancommunity.dto.PasswordRequest;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import com.web.mighigankoreancommunity.service.owner.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
@Tag(name = "Owner", description = "APIs for owner authentication and password management")
public class OwnerRestController {

    private final OwnerService ownerService;
    private final AuthenticationManager authenticationManager;

    public String emailToLowerCase(String email) {
        return email.trim().toLowerCase();
    }

    @Operation(summary = "Check if email is already registered", description = "Checks whether the given email is already associated with an existing owner account.")
    @PostMapping("/checkEmail")
    public boolean checkEmail(
            @RequestBody @Parameter(description = "Owner email") String email
    ) {
        return ownerService.getMemberByEmail(email);
    }

    @Operation(summary = "Get current owner profile", description = "Returns information about the currently logged-in owner.")
    @GetMapping("/me")
    public ResponseEntity<OwnerDTO> getCurrentUser(
            @AuthenticationPrincipal Owner owner
    ) {
        if (owner == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        OwnerDTO dto = ownerService.memberToDTO(owner);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Send password reset email", description = "Sends a password reset email to the owner's registered email address.")
    @PostMapping("/forgot/password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody @Parameter(description = "Owner email") String email
    ) {
        email = emailToLowerCase(email);
        if (ownerService.ownerForgotPasswordService(email)) {
            return ResponseEntity.ok("Owner password reset email sent.");
        } else {
            return ResponseEntity.badRequest().body("Email does not exist in our system.");
        }
    }

    @Operation(summary = "Reset owner password", description = "Resets the owner's password using the token sent to their email.")
    @PostMapping("/reset/password")
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "Reset token") @RequestParam("token") String token,
            @Parameter(description = "Owner email") @RequestParam("email") String email,
            @RequestBody @Parameter(description = "New password") PasswordRequest request
    ) {
        String password = request.getPassword();
        email = email.toLowerCase();

        if (ownerService.isPasswordTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired. Please request a new reset email.");
        }

        if (!ownerService.isTokenValidForEmail(token, email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token or email.");
        }

        ownerService.resetPassword(email, password);
        return ResponseEntity.ok("Password reset successful.");
    }


    @PostMapping("/api/owner/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest req) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        try {
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // 세션 저장
            HttpSession session = req.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            return ResponseEntity.ok().build(); // 200 OK
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
