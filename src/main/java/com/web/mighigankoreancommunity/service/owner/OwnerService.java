package com.web.mighigankoreancommunity.service.owner;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.OwnerDTO;
import com.web.mighigankoreancommunity.entity.Employee;
import com.web.mighigankoreancommunity.entity.Invitation;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.PasswordToken;
import com.web.mighigankoreancommunity.repository.PasswordTokenRepository;
import com.web.mighigankoreancommunity.repository.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final PasswordTokenRepository passwordTokenRepository;


    //    Check Expire Date
    public boolean isPasswordTokenExpired(String token) {
        PasswordToken passwordToken = passwordTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Password Token not found"));
        LocalDateTime expiresAt= passwordToken.getExpiresAt();
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isTokenValidForEmail(String token, String email) {
        return passwordTokenRepository.findByToken(token)
                .map(t -> t.getOwner().getOwnerEmail().equals(email))
                .orElse(false);
    }


//    Register Method
    public Long saveOwner(Owner owner) {
        String rawPassword = owner.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        owner.setOwnerPassword(encodedPassword);
        owner.setMemberRole(MemberRole.OWNER);
        ownerRepository.save(owner);
        return owner.getId();
    }

//    check Email
    public boolean getMemberByEmail(String email) {
        return ownerRepository.existsByOwnerEmail(email);
    }


    public OwnerDTO memberToDTO(Owner owner) {
        OwnerDTO dto = new OwnerDTO(
                owner.getId(),
                owner.getOwnerName(),
                owner.getOwnerEmail()
        );
        return dto;
    }

    private void sendRestPasswordEmailToUser(Owner owner, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(owner.getOwnerEmail());
        message.setSubject("This is password reset link");
        message.setText("Your password reset Link is :  \n\n" + link);
        message.setFrom("restoflowing@gmail.com");
        mailSender.send(message);
    }


    public boolean ownerForgotPasswordService(String email) {
        Optional<Owner> ownerOpt = ownerRepository.findOwnerByOwnerEmail(email);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            String token = UUID.randomUUID().toString();
            String resetLink = "http://127.0.0.1:10000/page/owner/reset/password?token=" + token + "&email=" + email;
//            String resetLink = "https://www.restoflowing.com/page/owner/reset/password?token=" + token + "&email=" + email;

            LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

            // 1. 해당 owner의 토큰 존재 여부 확인
            Optional<PasswordToken> tokenOpt = passwordTokenRepository.findByOwner(owner);

            PasswordToken passwordToken;
            if (tokenOpt.isPresent()) {
                // 2. 이미 있는 경우 업데이트
                passwordToken = tokenOpt.get();
                passwordToken.setToken(token);
                passwordToken.setExpiresAt(expiresAt);
            } else {
                // 3. 없는 경우 새로 생성
                passwordToken = PasswordToken.builder()
                        .token(token)
                        .owner(owner)
                        .expiresAt(expiresAt)
                        .build();
            }

            passwordTokenRepository.save(passwordToken);

            // 4. 이메일 전송
            sendRestPasswordEmailToUser(owner, resetLink);
            return true;
        }

        return false;
    }


    public void resetPassword(String email, String password) {
        Owner owner = ownerRepository.findOwnerByOwnerEmail(email).orElseThrow(() -> new RuntimeException("Owner not found"));
        String newPassword = passwordEncoder.encode(password);
        owner.setOwnerPassword(newPassword);
        ownerRepository.save(owner);
    }





}
