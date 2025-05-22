package com.web.mighigankoreancommunity.controller;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.auth.JwtResponse;
import com.web.mighigankoreancommunity.dto.auth.LoginRequest;
import com.web.mighigankoreancommunity.dto.auth.RegisterRequest;
import com.web.mighigankoreancommunity.dto.auth.UserInfoDto;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.jwt.JwtTokenProvider;
import com.web.mighigankoreancommunity.service.owner.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final OwnerService ownerService;


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


    @PostMapping("/login/owner")
    public ResponseEntity<?> loginOwner(@RequestBody LoginRequest request) {
        Long id = null;
        if (request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email and password must not be empty"));
        }

        // 1) 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        // 2) 사용자 정보 추출
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        if(user.isOwner()){
            id = user.getOwner().getId();
        } else if(user.isEmployee()){
            id = user.getEmployee().getId();
        } else {
            throw new RuntimeException("Invalid user");
        }
        MemberRole role = user.getCurrentMemberRole();
        String email = user.getUsername();

        // 3) 토큰 생성
        String token = jwtTokenProvider.createToken(email, role, id);

        return ResponseEntity.ok(new JwtResponse(token, role, id));
    }

    @PostMapping("/login/employee")
    public ResponseEntity<JwtResponse> loginEmployee(@RequestBody LoginRequest request) {
        Long id = null;
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        if(user.isOwner()){
            id = user.getOwner().getId();
        } else if(user.isEmployee()){
            id = user.getEmployee().getId();
        } else {
            throw new RuntimeException("Invalid user");
        }
        MemberRole role = user.getCurrentMemberRole();
        String email = user.getUsername();

        // 3) 토큰 생성
        String token = jwtTokenProvider.createToken(email, role, id);

        return ResponseEntity.ok(new JwtResponse(token, role, id));
    }


    @PostMapping("/register/owner")
    public String registerOwner(@RequestBody RegisterRequest registerRequest) {
        System.out.println(registerRequest.getEmail());
        ownerService.saveOwner(registerRequest);
        return "user/owner-login";
    }
}
