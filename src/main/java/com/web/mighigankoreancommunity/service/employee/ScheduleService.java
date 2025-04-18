package com.web.mighigankoreancommunity.service.employee;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.EmployeeDTO;
import com.web.mighigankoreancommunity.dto.ScheduleDTO;
import com.web.mighigankoreancommunity.entity.*;
import com.web.mighigankoreancommunity.error.RestaurantNotFoundException;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.employee.EmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.RestaurantEmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        List<RestaurantEmployee> restaurantEmployeeList = restaurantEmployeeRepository.findRestaurantEmployeesByRestaurant_Id(restaurantId)
                .orElseThrow(()-> new RuntimeException("Restaurant Employee not found."));
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        List<EmployeeDTO> kitchenList = new ArrayList<>();
        List<EmployeeDTO> serverList = new ArrayList<>();


        EmployeeDTO employeeDTO = new EmployeeDTO();

        restaurantEmployeeList.forEach(restaurantEmployee -> {
           Restaurant restaurant = restaurantEmployee.getRestaurant();
           Employee employee = restaurantEmployee.getEmployee();
            employeeDTO.setMemberRole(restaurantEmployee.getMemberRole());
            employeeDTO.setName(employee.getName());
            employeeDTO.setEmail(employee.getEmail());
            employeeDTO.setId(employee.getId());
           if (employeeDTO.getMemberRole() == MemberRole.KITCHEN || employeeDTO.getMemberRole() == MemberRole.MANAGER || employeeDTO.getMemberRole() == MemberRole.EMPLOYEE) {
               kitchenList.add(employeeDTO);
           } else{
               serverList.add(employeeDTO);
           }


           restaurantEmployee.getSchedules().forEach(schedule -> {
               ScheduleDTO scheduleDTO = new ScheduleDTO();
               System.out.println(schedule.getShift());
               scheduleDTO.setShift(schedule.getShift());
               scheduleDTO.setRestaurantId(restaurant.getId());
               scheduleDTO.setEmployeeId(employee.getId());
               scheduleDTO.setName(employee.getName());
               scheduleDTO.setShiftStartDate(schedule.getStartShiftDate());
               scheduleDTO.setShiftEndDate(schedule.getEndShiftDate());
               scheduleDTOList.add(scheduleDTO);
           });

           employeeDTO.setSchedules(scheduleDTOList);
           scheduleDTOList.stream().forEach(System.out::println);

        });

        Map<String, List<EmployeeDTO>> result = new HashMap<>();
        result.put("kitchenList", kitchenList);
        result.put("serverList", serverList);


        return result;
    }




    @Transactional
    public void scheduleSave(Long restaurantId, List<EmployeeDTO> employeeDTOList, Owner owner) {
        // 저장할 RestaurantEmployee, Schedule 객체들을 각각 리스트에 담음
        List<Schedule> scheduleList = new ArrayList<>();

        // employeeDTOList 순회
        for (EmployeeDTO employeeDTO : employeeDTOList) {
            // 각 직원마다 새 RestaurantEmployee 인스턴스 생성
            RestaurantEmployee restaurantEmployee = restaurantEmployeeRepository.findRestaurantEmployeeByRestaurant_IdAndEmployee_Id(restaurantId, employeeDTO.getId())
                    .orElseThrow(()-> new RuntimeException("Restaurant Employee not found."));

            Employee employee = restaurantEmployee.getEmployee();
            Restaurant restaurant = restaurantEmployee.getRestaurant();


            // DTO에 restaurantId가 없다면 파라미터의 restaurantId를 사용할 수도 있음.
            restaurantEmployee.setEmployee(employee);
            restaurantEmployee.setRestaurant(restaurant);
            restaurantEmployee.setMemberRole(employeeDTO.getMemberRole());

            List<Schedule> scheduleCheck  = scheduleRepository.findSchedulesByRestaurantEmployee(restaurantEmployee)
                    .orElseThrow(()-> new RuntimeException("Schedule not found."));

//            if an employee does not have schedule in DB
        if (scheduleCheck.isEmpty()) {
            for (ScheduleDTO scheduleDTO : employeeDTO.getSchedules()) {
                Schedule schedule = new Schedule(restaurantEmployee);
                schedule.setShift(scheduleDTO.getShift());
                schedule.setStartShiftDate(scheduleDTO.getShiftStartDate());
                schedule.setEndShiftDate(scheduleDTO.getShiftEndDate());

                // 필요한 경우 restaurantEmployee를 별도로 세팅해도 무방
                schedule.setRestaurantEmployee(restaurantEmployee);
                schedule.setEndShiftDate(employeeDTO.getShiftEndDate());
                schedule.setStartShiftDate(employeeDTO.getShiftStartDate());
                scheduleList.add(schedule);
            }
//            if employee has schedule
        } else {
            // DB에 스케줄이 있다면, 순서대로 employeeDTO의 ScheduleDTO와 매칭하여 업데이트
            // (순서와 건수가 동일하다는 전제 하에)
            int size = Math.min(employeeDTO.getSchedules().size(), scheduleCheck.size());
            for (int i = 0; i < size; i++) {
                ScheduleDTO scheduleDTO = employeeDTO.getSchedules().get(i);
                Schedule schedule = scheduleCheck.get(i);
                schedule.setShift(scheduleDTO.getShift());
                schedule.setStartShiftDate(employeeDTO.getShiftStartDate());
                schedule.setEndShiftDate(employeeDTO.getShiftEndDate());
                // 이미 DB에서 로드된 엔티티이므로 update 쿼리가 실행됨
                scheduleList.add(schedule);
            }
        }
        }

        if (!scheduleList.isEmpty()) {
            scheduleRepository.saveAll(scheduleList);
        }
    }
}
