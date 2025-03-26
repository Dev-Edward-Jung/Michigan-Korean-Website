package com.web.mighigankoreancommunity.controller.restaurant;



import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.member.MemberRepository;
import com.web.mighigankoreancommunity.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
@RestController
public class RestaurantRestController {
    private final RestaurantService restaurantService;

    @PostMapping("/save")
    public void saveRestaurant(@RequestBody Restaurant restaurant) {
        restaurantService.saveService(restaurant);
    }


    @GetMapping("/list")
    public List<Restaurant> restaurantList(@RequestBody Long ownerId) {
        List<Restaurant> restaurantList = restaurantService.restaurantListService(ownerId);
        return restaurantList;
    }
}
