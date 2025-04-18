package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.dto.RestaurantDTO;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.error.RestaurantNotFoundException;
import com.web.mighigankoreancommunity.error.UnauthorizedRestaurantAccessException;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    // ✅ 레스토랑 저장
    public void saveService(RestaurantDTO restaurantDTO, Owner owner) {
        String restaurantName = restaurantDTO.getRestaurantName();
        String restaurantCity = restaurantDTO.getRestaurantCity();

        Restaurant restaurant = Restaurant.create(restaurantName, restaurantCity, owner);
        restaurantRepository.save(restaurant);
    }

    // ✅ 로그인한 사용자의 레스토랑 목록 반환
    public List<RestaurantDTO> restaurantListService(Owner owner) {
        if (owner == null) {
            throw new UnauthorizedRestaurantAccessException("You are not authorized to access this service");
        }
        List<Restaurant> restaurantList = restaurantRepository.findRestaurantsByOwner(owner)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found."));
        List<RestaurantDTO> restaurantDTOList = new ArrayList<>();
        for (Restaurant restaurant : restaurantList) {
            RestaurantDTO restaurantDTO = new RestaurantDTO();
            restaurantDTO.setId(restaurant.getId());
            restaurantDTO.setRestaurantName(restaurant.getName());
            restaurantDTO.setRestaurantCity(restaurant.getCity());
            restaurantDTOList.add(restaurantDTO);
            System.out.println(restaurantDTO.toString());
        }
        return restaurantDTOList;

    }
}
