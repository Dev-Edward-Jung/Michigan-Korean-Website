package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.dto.restaurant.RestaurantDTO;
import com.web.mighigankoreancommunity.entity.Employee;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import com.web.mighigankoreancommunity.error.RestaurantNotFoundException;
import com.web.mighigankoreancommunity.error.UnauthorizedRestaurantAccessException;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
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
        }
        return restaurantDTOList;

    }

    public List<RestaurantDTO> restaurantListForEmployee(Employee employee) {
        return employee.getRestaurantEmployeeList().stream()
                .map(RestaurantEmployee::getRestaurant)
                .map(RestaurantDTO::from)
                .collect(Collectors.toList());
    }
}
