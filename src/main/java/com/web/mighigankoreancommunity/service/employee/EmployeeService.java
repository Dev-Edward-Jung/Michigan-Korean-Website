package com.web.mighigankoreancommunity.service.employee;

import com.web.mighigankoreancommunity.domain.InvitationStatus;
import com.web.mighigankoreancommunity.domain.Shift;
import com.web.mighigankoreancommunity.dto.employee.EmployeeDTO;
import com.web.mighigankoreancommunity.entity.*;
import com.web.mighigankoreancommunity.error.EmployeeNotFoundException;
import com.web.mighigankoreancommunity.error.InvitationNotFoundException;
import com.web.mighigankoreancommunity.error.RestaurantNotFoundException;
import com.web.mighigankoreancommunity.repository.PasswordTokenRepository;
import com.web.mighigankoreancommunity.repository.PayrollRepository;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.employee.EmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.InvitationRepository;
import com.web.mighigankoreancommunity.repository.employee.RestaurantEmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RestaurantRepository restaurantRepository;
    private final InvitationRepository invitationRepository;
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final ScheduleRepository scheduleRepository;
    private final PayrollRepository payrollRepository;

    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;


//    Check Expire Date
    public boolean isInvitationExpired(String token) {
        Invitation invitation = invitationRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invitation not found"));
        LocalDateTime expiresAt= invitation.getExpiresAt();
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isPasswordExpired(String token) {
        Employee employee = employeeRepository.findByPasswordToken_Token(token).orElseThrow(() -> new RuntimeException("Employee not found"));
        LocalDateTime expiresAt= employee.getPasswordToken().getExpiresAt();
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isTokenValidForEmail(String token, String email) {
        return passwordTokenRepository.findByToken(token)
                .map(t -> t.getEmployee().getEmail().equals(email))
                .orElse(false);
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
            rel.setApproved(false);
            Payroll payroll = new Payroll(dto.getHourlyWage(), rel);
            payroll = payrollRepository.save(payroll);
            rel.setPayroll(payroll);
            restaurantEmployeeRepository.save(rel);
        }

        // ✅ 6. 이메일 전송
        String invitationLink = "http://localhost:3000/auth/employee/register?token=" + token + "&restaurantId=" + restaurant.getId();;
//        server
//        String invitationLink = "https://www.restoflowing.com/page/employee/invited?token=" + token + "&restaurantId=" + restaurant.getId();
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
    public Long registerEmployee(String token, String rawPassword) {
        // 1. 초대 정보 조회
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new InvitationNotFoundException("Not valid Token"));

        // 2. 초대에 연결된 직원 확인
        Employee employee = invitation.getEmployee();
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee not found by token");
        }

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);
        employee.setPassword(encodedPassword);


        // ✅ 4. 해당 레스토랑과 직원 간의 관계 승인 처리
        Long restaurantId = invitation.getRestaurant().getId(); // 초대된 레스토랑 ID
        RestaurantEmployee restaurantEmployee = restaurantEmployeeRepository
                .findByEmployeeIdAndRestaurantId(employee.getId(), restaurantId)
                .orElseThrow(() -> new RuntimeException("RestaurantEmployee relation not found"));

        restaurantEmployee.setApproved(true); // 승인 처리
//        2주치 스케줄 넣어줌
        createInitialScheduleForEmployee(restaurantEmployee);

        // 5. 초대 상태 변경
        invitation.setStatus(InvitationStatus.ACCEPTED);

        // 6. 저장
        employeeRepository.save(employee);
        invitationRepository.save(invitation);
        restaurantEmployeeRepository.save(restaurantEmployee);




        log.info("✅ complete Employee: email={}, employeeId={}", employee.getEmail(), employee.getId());
        return employee.getId();
    }


    public boolean employeeForgotPasswordService(String email) {
        Optional<Employee> employeeOpt = employeeRepository.findEmployeeByEmail(email);

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            String token = UUID.randomUUID().toString();
//            String resetLink = "http://localhost:3000/auth/reset?token=" + token;
            String resetLink = "https://www.restoflowing.com/auth/reset?token=" + token + "&email=" + email;

            LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

            // 1. 해당 owner의 토큰 존재 여부 확인
            Optional<PasswordToken> tokenOpt = passwordTokenRepository.findByEmployee(employee);

            PasswordToken passwordToken;
            if (tokenOpt.isPresent()) {
                // 2. 이미 있는 경우 업데이트
                passwordToken = tokenOpt.get();
                passwordToken.setToken(token);
                passwordToken.setExpiresAt(expiresAt);
                passwordToken.setUsed(false);
            } else {
                // 3. 없는 경우 새로 생성
                passwordToken = PasswordToken.builder()
                        .token(token)
                        .employee(employee)
                        .expiresAt(expiresAt)
                        .used(false)
                        .build();
            }

            passwordTokenRepository.save(passwordToken);

            // 4. 이메일 전송
            sendRestPasswordEmailToUser(employee, resetLink);
            return true;
        }

        return false;
    }


    private void sendRestPasswordEmailToUser(Employee employee, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(employee.getEmail());
        message.setSubject("This is password reset link");
        message.setText("Your password reset Link is :  \n\n" + link);
        message.setFrom("restoflowing@gmail.com");
        mailSender.send(message);
    }


    public void resetPassword(String email, String password) {
        Employee employee = employeeRepository.findEmployeeByPasswordToken_Token(email).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        PasswordToken passwordToken = passwordTokenRepository.findByEmployee(employee).orElseThrow(RuntimeException::new);
        passwordToken.setUsed(true);
        String newPassword = passwordEncoder.encode(password);

        employee.setPassword(newPassword);
        passwordTokenRepository.save(passwordToken);
        employeeRepository.save(employee);
    }


    public List<EmployeeDTO> getAllEmployees(Long restaurantId, Owner owner) {
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(restaurantId, owner)
                .orElseThrow(RestaurantNotFoundException::new);

        List<RestaurantEmployee> employeeList = restaurantEmployeeRepository
                .findRestaurantEmployeesByRestaurant_IdAndApprovedTrue(restaurant.getId())
                .orElse(Collections.emptyList());

        return employeeList.stream()
                .map(emp ->new EmployeeDTO(
                        emp.getId(),
                        emp.getEmployee().getName(),
                        emp.getEmployee().getEmail(),
                        emp.getMemberRole(),
                        emp.getRestaurant().getId()
                        )
                ).collect(Collectors.toList());
    }



    public void createInitialScheduleForEmployee(RestaurantEmployee restaurantEmployee) {
        List<Schedule> schedules = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            Schedule offSchedule = Schedule.builder()
                    .restaurantEmployee(restaurantEmployee)
                    .shift(Shift.OFF)
                    .build();
            schedules.add(offSchedule);
        }

        scheduleRepository.saveAll(schedules);
    }

    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }


    public boolean existsEmployeeByPasswordToken(String token) {
        return employeeRepository.existsByPasswordToken_Token(token);
    }
}

