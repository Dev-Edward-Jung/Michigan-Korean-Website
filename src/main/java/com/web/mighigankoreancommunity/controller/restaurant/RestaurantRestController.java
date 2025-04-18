package com.web.mighigankoreancommunity.controller.restaurant;



import com.web.mighigankoreancommunity.dto.RestaurantDTO;
import com.web.mighigankoreancommunity.entity.CustomUserDetails;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.service.RestaurantService;
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
    public List<RestaurantDTO> restaurantList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return restaurantService.restaurantListService(userDetails.getOwner());
    }
}
