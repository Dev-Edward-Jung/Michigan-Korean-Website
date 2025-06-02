package com.web.mighigankoreancommunity.service.owner;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.OwnerDTO;
import com.web.mighigankoreancommunity.dto.auth.RegisterRequest;
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
                .map(t -> t.getOwner().getEmail().equals(email))
                .orElse(false);
    }


//    Register Method
    public Long saveOwner(RegisterRequest registerRequest) {
        Owner owner = Owner.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .ownerName(registerRequest.getName())
                .memberRole(MemberRole.OWNER)
                .build();
        Owner savedOwner = ownerRepository.save(owner);
        return savedOwner.getId();
    }

//    check Email
    public boolean getMemberByEmail(String email) {
        return ownerRepository.existsByEmail(email);
    }


    public OwnerDTO memberToDTO(Owner owner) {
        OwnerDTO dto = new OwnerDTO(
                owner.getId(),
                owner.getOwnerName(),
                owner.getEmail()
        );
        return dto;
    }

    private void sendRestPasswordEmailToUser(Owner owner, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(owner.getEmail());
        message.setSubject("This is password reset link");
        message.setText("Your password reset Link is :  \n\n" + link);
        message.setFrom("restoflowing@gmail.com");
        mailSender.send(message);
    }


    public boolean ownerForgotPasswordService(String email) {
        Optional<Owner> ownerOpt = ownerRepository.findOwnerByEmail(email);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            String token = UUID.randomUUID().toString();
            String resetLink = "http://localhost:3000/auth/reset/?token=" + token;
//            String resetLink = "https://www.restoflowing.com/auth/reset?token=" + token + "&email=" + email;
            System.out.println("owner : " + owner.getEmail());
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
            owner.setPasswordToken(passwordToken);
            System.out.println("Owner Email is : " + passwordToken.getOwner().getEmail());
            ownerRepository.save(owner);
            passwordTokenRepository.save(passwordToken);

            // 4. 이메일 전송
            sendRestPasswordEmailToUser(owner, resetLink);
            return true;
        }

        return false;
    }


    public void resetPassword(String token, String password) {
        Owner owner = ownerRepository.findOwnerByPasswordToken_Token(token).orElseThrow(() -> new RuntimeException("Owner not found"));
        PasswordToken passwordToken = passwordTokenRepository.findByOwner(owner).orElseThrow(RuntimeException::new);
        String newPassword = passwordEncoder.encode(password);

        passwordToken.setUsed(true);
        owner.setPassword(newPassword);


        passwordTokenRepository.save(passwordToken);
        ownerRepository.save(owner);
    }


    public boolean existsByEmail(String email) {
        return ownerRepository.existsByEmail(email);
    }

    public boolean existsOwnerByPasswordToken(String token) {
        return ownerRepository.existsByPasswordToken_Token(token);
    }




}
