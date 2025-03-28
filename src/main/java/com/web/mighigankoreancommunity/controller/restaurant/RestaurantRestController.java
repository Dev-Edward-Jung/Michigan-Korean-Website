package com.web.mighigankoreancommunity.controller.restaurant;



import com.web.mighigankoreancommunity.dto.MemberDTO;
import com.web.mighigankoreancommunity.dto.RestaurantDTO;
import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.member.MemberRepository;
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
                               @AuthenticationPrincipal Member member) {
        restaurantService.saveService(dto, member);
    }


    @GetMapping("/list")
    public List<Restaurant> restaurantList(@AuthenticationPrincipal Member member) {
        return restaurantService.restaurantListService(member);
    }
}
