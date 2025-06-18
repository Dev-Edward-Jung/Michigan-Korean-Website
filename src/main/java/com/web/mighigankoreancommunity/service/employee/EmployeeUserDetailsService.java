package com.web.mighigankoreancommunity.service.employee;


import com.web.mighigankoreancommunity.entity.Employee;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.repository.employee.EmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.RestaurantEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmailWithRestaurants(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found."));

        System.out.println("Employee 호출됨!");

        // ✅ 현재 로그인 시점에서 사용할 restaurantEmployee 하나 선택
        RestaurantEmployee selectedRel = employee.getRestaurantEmployeeList()
                .stream()
                .findFirst() // 또는 너가 정한 기준에 맞게 선택
                .orElse(null);

        CustomUserDetails userDetails = new CustomUserDetails(employee);
        userDetails.setRestaurantEmployee(selectedRel); // ✅ 핵심 포인트

        return userDetails;
    }

    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }
}
