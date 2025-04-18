package com.web.mighigankoreancommunity.service.employee;

import com.web.mighigankoreancommunity.domain.InvitationStatus;
import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.EmployeeDTO;
import com.web.mighigankoreancommunity.dto.InvitationDTO;
import com.web.mighigankoreancommunity.entity.*;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


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




    public String sendInvitationEmail(EmployeeDTO employeeDTO, Long ownerId) {
        String name = employeeDTO.getName();
        String email = employeeDTO.getEmail();
        MemberRole role = employeeDTO.getMemberRole();
        Restaurant restaurant = restaurantRepository.findById(employeeDTO.getRestaurantId()).
                orElseThrow(()->new RuntimeException("Restaurant not found"));
        Employee employee = employeeRepository.findEmployeeByEmail(email)
                .orElseThrow(()->new RuntimeException("Employee not found"));
//        Create Token
        String inviteToken = UUID.randomUUID().toString();
        Invitation invitation = new Invitation();

        if (employee == null) {
            employee = new Employee(name, email, null);
        } else {
            employee.setName(name);
        }

        employeeRepository.save(employee);

        invitation.setRestaurant(restaurant);
        invitation.setInvitedBy(ownerId);
        invitation.setToken(inviteToken);
        invitation.setExpiresAt(LocalDateTime.now().plusHours(24));
        invitation.setEmployee(employee);
        invitationRepository.save(invitation);

        employee.setInvitation(invitation);
        employeeRepository.save(employee);


//      invitation link need to be changed
//        String invitationLink = "https://restoflowing.com/page/employee/invited?token=" + inviteToken;
        String invitationLink = "http://127.0.0.1:10000/page/employee/invited?token=" + inviteToken;

//        Message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(employee.getEmail());
        message.setSubject("Welcome! " + employee.getName() + " You are invited by " + restaurant.getName());
        message.setText("You can now join your account with link below! \n\n" + "Your Invitation Link is : " + invitationLink);
        message.setFrom("restoflowing@gmail.com");


//        save employee
        RestaurantEmployee restaurantEmployee = new RestaurantEmployee();
        restaurantEmployee.setEmployee(employee);
        restaurantEmployee.setRestaurant(restaurant);
        restaurantEmployee.setMemberRole(role);

//        employee role save and employee save
        employeeRepository.save(employee);
        restaurantEmployeeRepository.save(restaurantEmployee);



        mailSender.send(message);

        System.out.println("Email sent");
        return invitationLink;
    }


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
//        Should check with owner
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        List<RestaurantEmployee> restaurantEmployeeList = restaurantEmployeeRepository.findRestaurantEmployeesByRestaurant_Id(restaurant.getId())
                .orElseThrow(()->new RuntimeException("Restaurant not found"));
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        restaurantEmployeeList.forEach(restaurantEmployee -> {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setMemberRole(restaurantEmployee.getMemberRole());
            employeeDTO.setEmail(restaurantEmployee.getEmployee().getEmail());
            employeeDTO.setName(restaurantEmployee.getEmployee().getName());
            employeeDTOList.add(employeeDTO);
        });


        return employeeDTOList;
    }
}

