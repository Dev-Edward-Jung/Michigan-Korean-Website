package com.web.mighigankoreancommunity.service.employee;

import com.web.mighigankoreancommunity.dto.EmployeeDTO;
import com.web.mighigankoreancommunity.dto.InvitationDTO;
import com.web.mighigankoreancommunity.entity.Invitation;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.employee.EmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RestaurantRepository restaurantRepository;
    private final InvitationRepository invitationRepository;

    private final JavaMailSender mailSender;


//    Check Expire Date
    public boolean isInvitationExpired(String token) {
        Invitation invitation = invitationRepository.findByToken(token);
        LocalDateTime expiresAt= invitation.getExpiresAt();
        return LocalDateTime.now().isAfter(expiresAt);
    }

//    get information from repository
    public Invitation getInvitationInfoByToken(String token) {
        return invitationRepository.findByToken(token);
    }




    public String sendInvitationEmail(EmployeeDTO employeeDTO, Long ownerId) {
        // 실제 이메일 전송 로직 대신 로그 출력
        // 실제 구현 시, JavaMailSender 등을 사용하여 이메일 전송
        Restaurant restaurant = restaurantRepository.findById(employeeDTO.getRestaurantId()).get();

//        Create Token
        String inviteToken = UUID.randomUUID().toString();
        Invitation invitation = new Invitation();

        invitation.setEmail(employeeDTO.getEmail());
        invitation.setRestaurant(restaurant);
        invitation.setInvitedBy(ownerId);
        invitation.setRole(employeeDTO.getMemberRole());
        invitation.setToken(inviteToken);
        invitation.setExpiresAt(LocalDateTime.now().plusHours(24));

        String invitationLink = "https://restoflowing.com/page/employee/invited?token=" + inviteToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(invitation.getEmail());
        message.setSubject("Welcome! You are invited by " + restaurant.getName());
        message.setText("You can now join your account with link below:\n");
        message.setText("Your Invitation Link is: " + invitationLink);
        message.setFrom("restoflowing@gmail.com");
        System.out.println(message);


        mailSender.send(message);
        invitationRepository.save(invitation);

        System.out.println("Email sent");
        return invitationLink;
    }
}
