package com.web.mighigankoreancommunity.service.owner;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.OwnerDTO;
import com.web.mighigankoreancommunity.entity.Employee;
import com.web.mighigankoreancommunity.entity.Invitation;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.repository.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

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

    private void sendInvitationEmailToUser(Owner owner, String restaurantName, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(owner.getOwnerEmail());
        message.setSubject("Welcome! You are invited to " + restaurantName);
        message.setText("Please use the following link to join (valid for 24 hours):\n\n" + link);
        message.setFrom("restoflowing@gmail.com");
        mailSender.send(message);
    }


    public boolean ownerForgotPasswordService(String email){
        Optional<Owner> ownerOpt = ownerRepository.findOwnerByOwnerEmail(email);
        if(ownerOpt.isPresent()){
            Owner owner = ownerOpt.get();
            String token = UUID.randomUUID().toString();
            String resetLink = "http://127.0.0.1:10000/page/owner/forgot/password?token=" + token;
//            String resetLink = "https://restoflowing.com/owner/forgot/password?token=" + token;
            sendInvitationEmailToUser(owner, email, resetLink);
            return true;
        } else{
            return false;
        }
    }






}
