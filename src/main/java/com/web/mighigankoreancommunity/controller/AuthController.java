package com.web.mighigankoreancommunity.controller;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.auth.JwtResponse;
import com.web.mighigankoreancommunity.dto.auth.LoginRequest;
import com.web.mighigankoreancommunity.dto.auth.RegisterRequest;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.jwt.JwtTokenProvider;
import com.web.mighigankoreancommunity.service.owner.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final OwnerService ownerService;


    @PostMapping("/login/owner")
    public ResponseEntity<?> loginOwner(@RequestBody LoginRequest request) {
        System.out.println(request.getEmail());
        String token = jwtTokenProvider.createToken(request.getEmail(), MemberRole.OWNER);
        System.out.println(token);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/login/employee")
    public ResponseEntity<?> loginEmployee(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtTokenProvider.createToken(authentication.getName(), MemberRole.EMPLOYEE);
        return ResponseEntity.ok(new JwtResponse(token));
    }


    @PostMapping("/register/owner")
    public String registerOwner(@RequestBody RegisterRequest registerRequest) {
        System.out.println(registerRequest.getEmail());
        ownerService.saveOwner(registerRequest);
        return "user/owner-login";
    }
}
