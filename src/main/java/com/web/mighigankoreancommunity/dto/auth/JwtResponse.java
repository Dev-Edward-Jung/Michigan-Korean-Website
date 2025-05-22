package com.web.mighigankoreancommunity.dto.auth;

import com.web.mighigankoreancommunity.domain.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Member;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long id;
    private MemberRole role;

    public JwtResponse(String token, MemberRole role, Long id) {
        this.token = token;
        this.role = role;
        this.id = id;
    }
    public JwtResponse(String token) {
        this.token = token;
    }
}
