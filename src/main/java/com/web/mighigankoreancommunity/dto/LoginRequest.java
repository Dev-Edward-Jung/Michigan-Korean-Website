package com.web.mighigankoreancommunity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    public String email;
    public String password;
    public String passwordConfirm;
}
