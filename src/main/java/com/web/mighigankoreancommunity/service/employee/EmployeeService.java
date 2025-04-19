package com.web.mighigankoreancommunity.service.employee;

import com.web.mighigankoreancommunity.domain.InvitationStatus;
import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.EmployeeDTO;
import com.web.mighigankoreancommunity.dto.InvitationDTO;
import com.web.mighigankoreancommunity.entity.*;
import com.web.mighigankoreancommunity.error.RestaurantNotFoundException;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.employee.EmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.InvitationRepository;
import com.web.mighigankoreancommunity.repository.employee.RestaurantEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;



@RequiredArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RestaurantRepository restaurantRepository;
    private final InvitationRepository invitationRepository;
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;

    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;


//    Check Expire Date
    public boolean isInvitationExpired(String token) {
        Invitation invitation = invitationRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invitation not found"));
        LocalDateTime expiresAt= invitation.getExpiresAt();
        return LocalDateTime.now().isAfter(expiresAt);
    }

//    get information from repository
    public Invitation getInvitationInfoByToken(String token) {
        return invitationRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invitation not found"));
    }



    @Transactional
    public String sendInvitationEmail(EmployeeDTO dto, Long ownerId) {
        String email = dto.getEmail();
        String name = dto.getName();
        Long restaurantId = dto.getRestaurantId();

        // ✅ 1. 레스토랑 조회 & 권한 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("레스토랑을 찾을 수 없습니다."));

        // ✅ 2. 직원 조회 or 생성
        Employee employee = employeeRepository.findEmployeeByEmail(email)
                .orElseGet(() -> new Employee(name, email, null));
        employee.setName(name);  // 이름 최신화

        // ✅ 3. 초대 생성 또는 재설정
        String token = UUID.randomUUID().toString();
        Invitation invitation = employee.getInvitation();

        if (invitation == null) {
            invitation = new Invitation();
            invitation.setEmployee(employee); // 양방향 설정
        }

        invitation.setRestaurant(restaurant);
        invitation.setToken(token);
        invitation.setInvitedBy(ownerId);
        invitation.setExpiresAt(LocalDateTime.now().plusHours(24));
        employee.setInvitation(invitation); // 다시 설정 (양방향)

        // ✅ 4. 저장
        employeeRepository.save(employee);
        invitationRepository.save(invitation);

        // ✅ 5. Restaurant-Employee 관계 추가 (없을 경우에만)
        boolean exists = restaurantEmployeeRepository
                .existsByEmployee_EmailAndRestaurant_Id(email, restaurantId);

        if (!exists) {
            RestaurantEmployee rel = new RestaurantEmployee();
            rel.setEmployee(employee);
            rel.setRestaurant(restaurant);
            rel.setMemberRole(dto.getMemberRole());
            restaurantEmployeeRepository.save(rel);
        }

        // ✅ 6. 이메일 전송
        String invitationLink = "http://127.0.0.1:10000/page/employee/invited?token=" + token;
//        server
//        String invitationLink = "https://restoflowing/page/employee/invited?token=" + token;
        sendInvitationEmailToUser(employee, restaurant.getName(), invitationLink);

        return invitationLink;
    }

    private void sendInvitationEmailToUser(Employee employee, String restaurantName, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(employee.getEmail());
        message.setSubject("Welcome! You are invited to " + restaurantName);
        message.setText("Please use the following link to join (valid for 24 hours):\n\n" + link);
        message.setFrom("restoflowing@gmail.com");
        mailSender.send(message);
    }

    @Transactional
    public boolean registerEmployeeService(String token, String password){
//        find Employee by token
        Employee employee = employeeRepository.findEmployeeByInvitation_Token(token);
        String encodedPassword = passwordEncoder.encode(password);
        employee.setPassword(encodedPassword);
        employee.setApproved(true);
        
//      later error define
        Invitation invitation = invitationRepository.findByToken(token).orElseThrow(()->new RuntimeException("Invitation not found"));
        invitation.setStatus(InvitationStatus.ACCEPTED);
        employeeRepository.save(employee);

        return true;
    }


    public List<EmployeeDTO> getAllEmployees(Long restaurantId, Owner owner) {
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(restaurantId, owner)
                .orElseThrow(RestaurantNotFoundException::new);

        List<RestaurantEmployee> employeeList = restaurantEmployeeRepository
                .findRestaurantEmployeesByRestaurant_Id(restaurant.getId())
                .orElse(Collections.emptyList());

        return employeeList.stream()
                .filter(RestaurantEmployee::isActive)
                .map(emp ->new EmployeeDTO(
                        emp.getId(),
                        emp.getEmployee().getName(),
                        emp.getEmployee().getEmail(),
                        emp.getMemberRole(),
                        emp.getRestaurant().getId()
                        )
                ).collect(Collectors.toList());
    }
}

