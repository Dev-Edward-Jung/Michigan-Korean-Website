package com.web.mighigankoreancommunity.controller.employeeSchedule;

import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/page/employee/schedule")
@Controller
public class employeeScheduleController {

    @GetMapping("/list")
    public String employeeSchedule(@RequestParam Long restaurantId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return "restaurant/employee-schedule";
    }

    @GetMapping("/edit")
    public String employeeScheduleEdit(@RequestParam Long restaurantId,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return "restaurant/employee-schedule-edit";
    }
}
