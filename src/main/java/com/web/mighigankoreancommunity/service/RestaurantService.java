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
    public RestaurantDTO saveService(RestaurantDTO restaurantDTO, Owner owner) {
        String restaurantName = restaurantDTO.getRestaurantName();
        String restaurantCity = restaurantDTO.getRestaurantCity();

        Restaurant restaurant = Restaurant.create(restaurantName, restaurantCity, owner);
        Restaurant saved = restaurantRepository.save(restaurant);
        return RestaurantDTO.from(saved);
    }

    // ✅ 로그인한 사용자의 레스토랑 목록 반환
    public List<RestaurantDTO> restaurantListService(Owner owner) {
        validateOwner(owner);

        List<Restaurant> restaurantList = restaurantRepository.findRestaurantsByOwner(owner)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found."));

        return restaurantList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Owner 유효성 검사 메서드
    private void validateOwner(Owner owner) {
        if (owner == null) {
            throw new UnauthorizedRestaurantAccessException("You are not authorized to access this service");
        }
    }

    // 🔹 Restaurant → RestaurantDTO 변환 메서드
    private RestaurantDTO convertToDTO(Restaurant restaurant) {
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getName())
                .restaurantCity(restaurant.getCity())
                .build();
    }

    public List<RestaurantDTO> restaurantListForEmployee(Employee employee) {
        return employee.getRestaurantEmployeeList().stream()
                .map(RestaurantEmployee::getRestaurant)
                .map(RestaurantDTO::from)
                .collect(Collectors.toList());
    }
}
