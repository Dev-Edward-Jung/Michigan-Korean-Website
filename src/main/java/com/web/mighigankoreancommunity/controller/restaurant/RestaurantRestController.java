package com.web.mighigankoreancommunity.controller.restaurant;



import ch.qos.logback.core.model.Model;
import com.web.mighigankoreancommunity.dto.RestaurantDTO;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.RestaurantService;
import com.web.mighigankoreancommunity.service.employee.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
@RestController
public class RestaurantRestController {
    private final RestaurantService restaurantService;

    @PostMapping("/save")
    public void saveRestaurant(@RequestBody RestaurantDTO dto,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        restaurantService.saveService(dto, userDetails.getOwner());
    }


    @GetMapping("/list")
    @ResponseBody
    public List<RestaurantDTO> restaurantList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails.getOwner() != null) {
            // 사장님인 경우: 자신이 소유한 레스토랑 리스트 반환
            return restaurantService.restaurantListService(userDetails.getOwner());

        } else if (userDetails.getEmployee() != null) {
            // 직원인 경우: 소속된 레스토랑 리스트 반환
            return restaurantService.restaurantListForEmployee(userDetails.getEmployee());

        } else {
            throw new RuntimeException("Invalid user: Not owner or employee.");
        }
    }
}
