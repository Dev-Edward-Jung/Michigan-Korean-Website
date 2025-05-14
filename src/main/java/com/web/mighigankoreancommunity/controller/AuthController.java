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
        if (request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email and password must not be empty"));
        }
        // 1) 인증을 시도하고, 성공 시 Authentication 객체를 얻는다
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        // 2) 반환된 객체의 getName() 혹은 getPrincipal()에서 실제 사용자 정보를 꺼낸다
        String email = authentication.getName();
        // 또는
        // CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        // String email = user.getUsername();

        System.out.println("When you login : Email : " + email);  // 여기선 요청한 email이 찍혀야 한다

        // 3) 토큰 발급 등 나머지 로직
        MemberRole role = ((CustomUserDetails)authentication.getPrincipal()).getCurrentMemberRole();
        String token = jwtTokenProvider.createToken(email, role);
        return ResponseEntity.ok(new JwtResponse(token, role));
    }

    @PostMapping("/login/employee")
    public ResponseEntity<JwtResponse> loginEmployee(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        MemberRole actualRole = user.getCurrentMemberRole();
        String token = jwtTokenProvider.createToken(user.getUsername(), actualRole);
        return ResponseEntity.ok(new JwtResponse(token, actualRole));
    }


    @PostMapping("/register/owner")
    public String registerOwner(@RequestBody RegisterRequest registerRequest) {
        System.out.println(registerRequest.getEmail());
        ownerService.saveOwner(registerRequest);
        return "user/owner-login";
    }
}
