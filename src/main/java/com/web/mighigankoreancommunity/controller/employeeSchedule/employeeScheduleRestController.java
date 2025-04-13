package com.web.mighigankoreancommunity.controller.employeeSchedule;


import com.web.mighigankoreancommunity.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/employee/schedule")
@RestController
public class employeeScheduleRestController {

    @GetMapping("/list")
    public void getAllSchedule(@RequestParam Long restaurantId, @AuthenticationPrincipal CustomUserDetails user){

    }

    @PostMapping("/save")
    public void saveSchedule(@RequestParam Long restaurantId, @AuthenticationPrincipal CustomUserDetails user){

    }

}
