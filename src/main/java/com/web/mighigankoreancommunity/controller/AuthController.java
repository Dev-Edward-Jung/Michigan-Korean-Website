package com.web.mighigankoreancommunity.controller;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.ApiResponse;
import com.web.mighigankoreancommunity.dto.auth.*;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.PasswordToken;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.jwt.JwtTokenProvider;
import com.web.mighigankoreancommunity.service.PasswordTokenService;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import com.web.mighigankoreancommunity.service.employee.EmployeeUserDetailsService;
import com.web.mighigankoreancommunity.service.owner.OwnerService;
import com.web.mighigankoreancommunity.service.owner.OwnerUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final OwnerService ownerService;
    private final OwnerUserDetailsService ownerUserDetailsService;
    private final EmployeeUserDetailsService employeeUserDetailsService;

    private final EmployeeService employeeService;
    private final PasswordTokenService passwordTokenService;

    public String emailToLowerCase(String email) {
        return email.trim().toLowerCase();
    }

    // 1) @AuthenticationPrincipal 사용하기
    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> currentUser(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        UserInfoDto dto = new UserInfoDto(
                user.getCurrentMemberRole(),
                user.getUsername(),
                user.getCurrentRestaurantId()
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateJwt(@AuthenticationPrincipal CustomUserDetails user) {
        if (user != null) {
            return ResponseEntity.ok().build(); // 인증됨
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Check if email is already registered", description = "Checks whether the given email is already associated with an existing owner account.")
    @PostMapping("/checkEmail")
    public boolean checkEmail(
            @RequestBody @Parameter(description = "Owner email")EmailCheckRequest emailCheckRequest
    ) {
        System.out.println(emailCheckRequest.getEmail());
        String email = emailCheckRequest.getEmail().toLowerCase();
        return ownerService.getMemberByEmail(email);
    }

    @PostMapping("/register/owner")
    public void registerOwner(@RequestBody RegisterRequest request){
        ownerService.saveOwner(request);
    }


    @PostMapping("/login/owner")
    public ResponseEntity<?> loginOwner(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email and password must not be empty"));
        }

        try {
            // 🔍 Owner 이메일인지 먼저 체크
            if (!ownerUserDetailsService.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid email or password"));
            }

            // ✅ Owner만 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Long id = user.getOwner().getId();
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getCurrentMemberRole(), id);

            return ResponseEntity.ok(new JwtResponse(token, user.getCurrentMemberRole(), id));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid email or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "login failed"));
        }
    }

    @PostMapping("/login/employee")
    public ResponseEntity<?> loginEmployee(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email and password must not be empty"));
        }

        try {
            // 🔍 Employee email Checking
            if (!employeeUserDetailsService.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "invalid email or password"));
            }

            // ✅ Employee authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Long id = user.getEmployee().getId();
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getCurrentMemberRole(), id);

            return ResponseEntity.ok(new JwtResponse(token, user.getCurrentMemberRole(), id));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid email or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "login failed"));
        }
    }


    @Operation(summary = "Send password reset email", description = "Sends a password reset email to the owner's registered email address.")
    @PostMapping("/forget/password")
    public ResponseEntity<?> forget(
            @RequestBody @Parameter(description = "Owner email") String email
    ) {
        email = emailToLowerCase(email);
        if (ownerService.existsByEmail(email)) {
            ownerService.ownerForgotPasswordService(email);
            return ResponseEntity.ok().build();
        }
        else if (employeeService.existsByEmail(email)) {
            employeeService.employeeForgotPasswordService(email);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Reset owner password", description = "Resets the owner's password using the token sent to their email.")
    @PostMapping("/reset/password")
    public ResponseEntity<String> resetPassword(
            @RequestBody @Parameter(description = "New password") PasswordRequest request
    ) {
        String password = request.getPassword();
        String token  = request.getToken();

        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<PasswordToken> optionalToken = passwordTokenService.findByToken(token);
        if (optionalToken.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        PasswordToken resetToken = optionalToken.get();
        System.out.println("Rest TOken이 사용되었나?" + resetToken.isUsed());
//        Already Used Token
        if (resetToken.isUsed()) {
            return ResponseEntity.badRequest().build();
        }


//      Token is expired
        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("token : " + resetToken.getToken());
        System.out.println( "어디서 문제노 : " + ownerService.existsOwnerByPasswordToken(token));
        if(ownerService.existsOwnerByPasswordToken(token)) {
            ownerService.resetPassword(token, password);
            System.out.println("Owner는 지금  : " + ownerService.existsOwnerByPasswordToken(token));
            return ResponseEntity.ok().build();
        }
        else if (employeeService.existsEmployeeByPasswordToken(token)) {
            employeeService.resetPassword(token, password);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }





}
