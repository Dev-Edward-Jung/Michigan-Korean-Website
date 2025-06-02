package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.entity.PasswordToken;
import com.web.mighigankoreancommunity.repository.PasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordTokenService {
    private final PasswordTokenRepository  passwordTokenRepository;

    public Optional<PasswordToken> findByToken(String token) {
        return passwordTokenRepository.findByToken(token);
    }

}
