package com.web.mighigankoreancommunity.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Member;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String tokenType = "Bearer";
    private Member role;

    public JwtResponse(String token) {
        this.token = token;
    }
}
