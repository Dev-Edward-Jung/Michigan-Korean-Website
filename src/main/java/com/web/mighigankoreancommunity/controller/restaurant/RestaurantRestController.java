package com.web.mighigankoreancommunity.controller.restaurant;



import com.web.mighigankoreancommunity.dto.restaurant.RestaurantDTO;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "APIs for managing restaurants")
public class RestaurantRestController {

    private final RestaurantService restaurantService;

    @Operation(summary = "Create a new restaurant", description = "Creates a new restaurant for the currently logged-in owner.")
    @PostMapping("/save")
    public void saveRestaurant(
            @RequestBody @Parameter(description = "Restaurant data to save") RestaurantDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        restaurantService.saveService(dto, userDetails.getOwner());
    }

    @Operation(summary = "Get list of restaurants", description = "Returns a list of restaurants owned by the user (owner) or assigned to them (employee).")
    @GetMapping("/list")
    @ResponseBody
    public List<RestaurantDTO> restaurantList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails.getOwner() != null) {
            return restaurantService.restaurantListService(userDetails.getOwner());
        } else if (userDetails.getEmployee() != null) {
            return restaurantService.restaurantListForEmployee(userDetails.getEmployee());
        } else {
            throw new RuntimeException("Invalid user: Not owner or employee.");
        }
    }
}
