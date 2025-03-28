package com.web.mighigankoreancommunity.dto;


import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Data
public class RestaurantDTO {
    private long id;
    private String restaurantName;
    private String restaurantCity;
    private Long ownerId;



    @Builder
    public RestaurantDTO(long id, String restaurantName, String restaurantCity, Long ownerId) {
    this.id = id;
    this.restaurantName = restaurantName;
    this.restaurantCity = restaurantCity;
    this.ownerId = ownerId;
    }


}
