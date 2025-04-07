package com.web.mighigankoreancommunity.service.employee;

import com.web.mighigankoreancommunity.dto.InvitationDTO;
import com.web.mighigankoreancommunity.entity.Invitation;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.employee.EmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@RequiredArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RestaurantRepository restaurantRepository;
    private final InvitationRepository invitationRepository;

    public void sendInvitationEmail(InvitationDTO invitationDTO) {
        // 실제 이메일 전송 로직 대신 로그 출력
        System.out.println("Sending invitation email to " + invitationDTO.email + " with link: ");
        // 실제 구현 시, JavaMailSender 등을 사용하여 이메일 전송
        Restaurant restaurant = restaurantRepository.findById(invitationDTO.restaurantId).get();
        String inviteToken = UUID.randomUUID().toString();
        Invitation invitation = new Invitation();

        invitation.setEmail(invitationDTO.email);
        invitation.setRestaurant(restaurant);
        invitation.setInvitedBy(invitationDTO.ownerId);
        invitation.setRole(invitationDTO.memberRole);

        invitationRepository.save(invitation);
        String invitationLink = "https://restoflowing.com/invite?token=" + invitationDTO.token;
    }
}
