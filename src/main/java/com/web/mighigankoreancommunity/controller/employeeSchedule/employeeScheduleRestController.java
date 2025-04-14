package com.web.mighigankoreancommunity.controller.employeeSchedule;


import com.web.mighigankoreancommunity.dto.EmployeeDTO;
import com.web.mighigankoreancommunity.dto.ScheduleDTO;
import com.web.mighigankoreancommunity.entity.CustomUserDetails;
import com.web.mighigankoreancommunity.service.employee.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/employee/schedule")
@RestController
public class employeeScheduleRestController {
    private final ScheduleService scheduleService;

    @GetMapping("/list")
    public Map<String, List<EmployeeDTO>> getAllSchedule(@RequestParam Long restaurantId,
                                                         @AuthenticationPrincipal CustomUserDetails user){
        Map<String, List<EmployeeDTO>> result = scheduleService.findAllScheduleByRestaurantId(restaurantId, user.getOwner());
        return result;
    }

    @PostMapping("/save")
    public void saveSchedule(@RequestParam Long restaurantId, @RequestBody List<EmployeeDTO> employeeDTOList, @AuthenticationPrincipal CustomUserDetails user){
//        scheduleService.scheduleSave(restaurantId, employeeDTOList, user.getOwner());
//        Only have EmployeeId and MemberRole, and Shifts
        scheduleService.scheduleSave(restaurantId, employeeDTOList, user.getOwner());
    }

}
