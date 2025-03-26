package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public void saveService(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }


    public List<Restaurant> restaurantListService(Long ownerId) {
        return restaurantRepository.findRestaurantsByOwnerId(ownerId);
    }

}
