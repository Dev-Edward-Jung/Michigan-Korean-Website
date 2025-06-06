package com.web.mighigankoreancommunity.service.employee;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.employee.EmployeeDTO;
import com.web.mighigankoreancommunity.dto.schedule.RestaurantScheduleRequest;
import com.web.mighigankoreancommunity.dto.schedule.RestaurantScheduleResponse;
import com.web.mighigankoreancommunity.dto.schedule.ScheduleDTO;
import com.web.mighigankoreancommunity.entity.*;
import com.web.mighigankoreancommunity.error.RestaurantEmployeeNotFoundException;
import com.web.mighigankoreancommunity.error.RestaurantNotFoundException;
import com.web.mighigankoreancommunity.error.UnauthorizedRestaurantAccessException;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.employee.EmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.RestaurantEmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;
    private final RestaurantRepository restaurantRepository;


    @Transactional
    public RestaurantScheduleResponse findAllScheduleByRestaurantId(Long restaurantId, Owner owner) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);


        List<RestaurantEmployee> restaurantEmployees = restaurantEmployeeRepository
                .findRestaurantEmployeesByRestaurant_IdAndApprovedTrue(restaurantId)
                .orElseThrow(RestaurantEmployeeNotFoundException::new);

        List<EmployeeDTO> kitchenList = new ArrayList<>();
        List<EmployeeDTO> serverList = new ArrayList<>();

        for (RestaurantEmployee re : restaurantEmployees) {
            Employee employee = re.getEmployee();
            MemberRole role = re.getMemberRole();
            LocalDate startDate = re.getRestaurant().getStartShiftDate();
            LocalDate endDate = re.getRestaurant().getEndShiftDate();

            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setId(employee.getId());
            employeeDTO.setName(employee.getName());
            employeeDTO.setEmail(employee.getEmail());
            employeeDTO.setMemberRole(role);

            // ✅ 개별 스케줄 리스트 생성 (공용 리스트 쓰면 안 됨!)
            List<ScheduleDTO> individualSchedules = re.getSchedules().stream().map(schedule -> {
                ScheduleDTO s = new ScheduleDTO();
                s.setShift(schedule.getShift());
                s.setRestaurantId(restaurantId);
                s.setEmployeeId(employee.getId());
                s.setName(employee.getName());
                s.setShiftStartDate(startDate);
                s.setShiftEndDate(endDate);
                return s;
            }).toList();

            employeeDTO.setSchedules(individualSchedules);

            // ✅ 역할 기반 분리
            if (role == MemberRole.KITCHEN || role == MemberRole.MANAGER || role == MemberRole.EMPLOYEE) {
                kitchenList.add(employeeDTO);
            } else {
                serverList.add(employeeDTO);
            }
        }

        Map<String, List<EmployeeDTO>> result = new HashMap<>();
        result.put("kitchenList", kitchenList);
        result.put("serverList", serverList);

        return RestaurantScheduleResponse.builder()
                .startDate(restaurant.getStartShiftDate())
                .endDate(restaurant.getEndShiftDate())
                .employees(result)
                .build();
    }




    @Transactional
    public void scheduleSave(Long restaurantId, RestaurantScheduleRequest request, Owner owner) {
        // 1. 레스토랑 권한 체크
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(restaurantId, owner)
                .orElseThrow(() -> new UnauthorizedRestaurantAccessException("레스토랑 권한 없음"));
        List<Schedule> schedulesToSave = new ArrayList<>();
        restaurant.setStartShiftDate(request.getStartDate());
        restaurant.setEndShiftDate(request.getEndDate());
        restaurantRepository.save(restaurant);

        for (EmployeeDTO employeeDTO : request.getEmployees()) {
            Long employeeId = employeeDTO.getId();

            // 2. RestaurantEmployee 조회
            RestaurantEmployee restaurantEmployee = restaurantEmployeeRepository
                    .findRestaurantEmployeeByRestaurant_IdAndEmployee_Id(restaurantId, employeeId)
                    .orElseThrow(() -> new RestaurantEmployeeNotFoundException("직원 정보 없음"));

            List<ScheduleDTO> incomingSchedules = employeeDTO.getSchedules();
            if (incomingSchedules == null || incomingSchedules.isEmpty()) continue;

            // 3. 기존 스케줄 조회 (Optional 대신 List)
            List<Schedule> existingSchedules = scheduleRepository.findSchedulesByRestaurantEmployee(restaurantEmployee);

            if (existingSchedules.isEmpty()) {
                // 3-1. 신규 스케줄 저장
                for (ScheduleDTO dto : incomingSchedules) {
                    Schedule schedule = Schedule.builder()
                            .restaurantEmployee(restaurantEmployee)
                            .shift(dto.getShift())
                            .build();
                    schedulesToSave.add(schedule);
                }
            } else {
                // 3-2. 기존 스케줄 업데이트 (순서 기준. 이상적으론 날짜 기준 매칭이 바람직)
                int size = Math.min(existingSchedules.size(), incomingSchedules.size());
                for (int i = 0; i < size; i++) {
                    Schedule schedule = existingSchedules.get(i);
                    ScheduleDTO dto = incomingSchedules.get(i);

                    schedule.setShift(dto.getShift());
                    schedulesToSave.add(schedule);
                }
            }
        }

        // 4. 저장
        if (!schedulesToSave.isEmpty()) {
            scheduleRepository.saveAll(schedulesToSave);
        }
    }
}
