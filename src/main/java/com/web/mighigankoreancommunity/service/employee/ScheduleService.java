package com.web.mighigankoreancommunity.service.employee;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.EmployeeDTO;
import com.web.mighigankoreancommunity.dto.ScheduleDTO;
import com.web.mighigankoreancommunity.entity.*;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.employee.EmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.RestaurantEmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;
    private final RestaurantRepository restaurantRepository;


    public Map<String, List<EmployeeDTO>> findAllScheduleByRestaurantId(Long restaurantId, Owner owner) {
        List<RestaurantEmployee> restaurantEmployeeList = restaurantEmployeeRepository.findRestaurantEmployeesByRestaurant_Id(restaurantId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        List<EmployeeDTO> kitchenList = new ArrayList<>();
        List<EmployeeDTO> serverList = new ArrayList<>();

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        EmployeeDTO employeeDTO = new EmployeeDTO();

        restaurantEmployeeList.forEach(restaurantEmployee -> {
           Restaurant restaurant = restaurantEmployee.getRestaurant();
           Employee employee = restaurantEmployee.getEmployee();
            employeeDTO.setMemberRole(restaurantEmployee.getMemberRole());
            employeeDTO.setName(employee.getName());
            employeeDTO.setEmail(employee.getEmail());
            employeeDTO.setId(employee.getId());

            System.out.println(employeeDTO.getMemberRole());
           if (employeeDTO.getMemberRole() == MemberRole.KITCHEN || employeeDTO.getMemberRole() == MemberRole.MANAGER || employeeDTO.getMemberRole() == MemberRole.EMPLOYEE) {
               kitchenList.add(employeeDTO);
           } else{
               serverList.add(employeeDTO);
           }


           restaurantEmployee.getSchedules().forEach(schedule -> {
               scheduleDTO.setShift(schedule.getShift());
               scheduleDTO.setRestaurantId(restaurant.getId());
               scheduleDTO.setEmployeeId(employee.getId());
               scheduleDTO.setName(employee.getName());
               scheduleDTO.setShiftStartDate(schedule.getStartShiftDate());
               scheduleDTO.setShiftEndDate(schedule.getEndShiftDate());
               scheduleDTOList.add(scheduleDTO);
           });

           employeeDTO.setSchedules(scheduleDTOList);

        });

        Map<String, List<EmployeeDTO>> result = new HashMap<>();
        result.put("kitchenList", kitchenList);
        result.put("serverList", serverList);

        return result;
    }



    public void ScheduleSave(Long restaurantId, List<EmployeeDTO> employeeDTOList, Owner owner) {

    }
}
