package com.web.mighigankoreancommunity.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {
    public String email;
    public String password;
    public String passwordConfirm;
}
