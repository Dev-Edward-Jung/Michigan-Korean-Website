package com.web.mighigankoreancommunity.controller.restaurant;



import ch.qos.logback.core.model.Model;
import com.web.mighigankoreancommunity.dto.RestaurantDTO;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.RestaurantService;
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
    public List<RestaurantDTO> restaurantList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, HttpServletRequest request) {
        return restaurantService.restaurantListService(userDetails.getOwner());
    }
}
