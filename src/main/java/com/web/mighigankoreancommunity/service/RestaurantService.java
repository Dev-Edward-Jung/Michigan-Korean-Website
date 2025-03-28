package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.dto.RestaurantDTO;
import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.member.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    // ✅ 레스토랑 저장
    public void saveService(RestaurantDTO restaurantDTO, Member owner) {
        String restaurantName = restaurantDTO.getRestaurantName();
        String restaurantCity = restaurantDTO.getRestaurantCity();

        Restaurant restaurant = Restaurant.create(restaurantName, restaurantCity, owner);
        restaurantRepository.save(restaurant);
    }

    // ✅ 로그인한 사용자의 레스토랑 목록 반환
    public List<Restaurant> restaurantListService(Member owner) {
        return restaurantRepository.findRestaurantsByOwner(owner);
    }
}
