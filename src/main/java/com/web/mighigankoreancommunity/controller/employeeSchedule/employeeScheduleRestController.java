package com.web.mighigankoreancommunity.controller.employeeSchedule;


import com.web.mighigankoreancommunity.dto.employee.EmployeeDTO;
import com.web.mighigankoreancommunity.dto.schedule.RestaurantScheduleRequest;
import com.web.mighigankoreancommunity.dto.schedule.RestaurantScheduleResponse;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.employee.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee/schedule")
@RequiredArgsConstructor
@Tag(name = "Employee Schedule", description = "APIs for managing employee work schedules")
public class employeeScheduleRestController {

    private final ScheduleService scheduleService;

    @Operation(summary = "Get all employee schedules", description = "Returns a list of employee schedules grouped by shift type (e.g., morning, evening) for the specified restaurant.")
    @GetMapping("/list")
    public RestaurantScheduleResponse getAllSchedule(
            @Parameter(description = "Restaurant ID") @RequestParam Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return scheduleService.findAllScheduleByRestaurantId(restaurantId, user.getOwner());
    }

    @Operation(summary = "Save employee schedule", description = "Saves or updates the work schedule for employees in a given restaurant.")
    @PostMapping("/save")
    public void saveSchedule(
            @Parameter(description = "Restaurant ID") @RequestParam Long restaurantId,
            @RequestBody @Parameter(description = "List of employee schedule info (ID, role, shifts, etc.)")RestaurantScheduleRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        scheduleService.scheduleSave(restaurantId, request, user.getOwner());
    }
}
