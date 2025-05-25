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
            System.out.println("Employee: ");
            PayrollResponse payrollResponse = PayrollResponse.builder()
                    .hourlyWage(payroll.getHourlyWage())
                    .name(restaurantEmployee.getEmployee().getName())
                    .id(restaurantEmployee.getId())
                    .build();
            payrollResponses.add(payrollResponse);

        }

        return payrollResponses;
    }



    public PayrollResponse updatePayroll(Long restaurantId, CustomUserDetails customUserDetails, PayrollRequest payrollRequest) {
        return null;
    }

}
