package com.web.mighigankoreancommunity.controller;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.auth.JwtResponse;
import com.web.mighigankoreancommunity.dto.auth.LoginRequest;
import com.web.mighigankoreancommunity.dto.auth.RegisterRequest;
import com.web.mighigankoreancommunity.dto.auth.UserInfoDto;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.jwt.JwtTokenProvider;
import com.web.mighigankoreancommunity.service.employee.EmployeeUserDetailsService;
import com.web.mighigankoreancommunity.service.owner.OwnerService;
import com.web.mighigankoreancommunity.service.owner.OwnerUserDetailsService;
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

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final OwnerService ownerService;
    private final OwnerUserDetailsService ownerUserDetailsService;
    private final EmployeeUserDetailsService employeeUserDetailsService;


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
            // 🔍 Owner 이메일인지 먼저 체크
            if (!employeeUserDetailsService.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "invalid email or password"));
            }

            // ✅ Owner만 인증 시도
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


    @PostMapping("/register/owner")
    public String registerOwner(@RequestBody RegisterRequest registerRequest) {
        ownerService.saveOwner(registerRequest);
        return "user/owner-login";
    }
}
