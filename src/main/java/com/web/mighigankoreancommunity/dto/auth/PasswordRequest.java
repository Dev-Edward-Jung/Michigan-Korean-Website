package com.web.mighigankoreancommunity.dto.auth;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequest {
    private String password;
    private String token;
}
