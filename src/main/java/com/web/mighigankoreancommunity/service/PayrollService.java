package com.web.mighigankoreancommunity.service;

import com.web.mighigankoreancommunity.dto.payroll.PayrollRequest;
import com.web.mighigankoreancommunity.dto.payroll.PayrollResponse;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Payroll;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.repository.PayrollRepository;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.employee.RestaurantEmployeeRepository;
import com.web.mighigankoreancommunity.repository.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {
    private final PayrollRepository payrollRepository;
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;
    private final RestaurantRepository restaurantRepository;


    public List<PayrollResponse> getAllPayrolls(Long restaurantId, Owner owner) {
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(restaurantId, owner)
                .orElseThrow(RuntimeException::new);
        List<PayrollResponse> payrollResponses = new ArrayList<>();

        List<RestaurantEmployee> restaurantEmployeeList = restaurantEmployeeRepository
                .findRestaurantEmployeesByRestaurant_IdAndApprovedTrue(restaurant.getId())
                .orElse(Collections.emptyList());
        for (RestaurantEmployee restaurantEmployee : restaurantEmployeeList) {
            Payroll payroll = payrollRepository.findPayrollByRestaurantEmployee(restaurantEmployee)
                    .orElseThrow(RuntimeException::new);
            PayrollResponse payrollResponse = PayrollResponse.builder()
                    .hourlyWage(payroll.getHourlyWage())
                    .name(restaurantEmployee.getEmployee().getName())
                    .id(payroll.getId())
                    .totalWage(payroll.getTotalWage())
                    .build();
            payrollResponses.add(payrollResponse);

        }

        return payrollResponses;
    }



    public PayrollResponse updatePayroll(Long restaurantId, CustomUserDetails customUserDetails, PayrollRequest payrollRequest) {
        if (!customUserDetails.isOwner()) {
            throw new RuntimeException("Invalid user");
        }

        // 1. 레스토랑 유효성 검사
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(restaurantId, customUserDetails.getOwner())
                .orElseThrow(() -> new RuntimeException("Restaurant not found or access denied"));

        // 2. 기존 Payroll 찾기 (업데이트니까)
        Payroll payroll = payrollRepository.findById(payrollRequest.getId())
                .orElseThrow(() -> new RuntimeException("Payroll record not found"));

        // 3. 필드 업데이트
        payroll.setHourlyWage(payrollRequest.getHourlyWage());
        payroll.setTotalWage(payrollRequest.getTotalWage());

        // 4. 저장
        Payroll updated = payrollRepository.save(payroll);
        PayrollResponse payrollResponse = PayrollResponse.of(updated);
        payrollResponse.setName(payrollRequest.getName());
        // 5. 응답 반환
        return payrollResponse;
    }

}
