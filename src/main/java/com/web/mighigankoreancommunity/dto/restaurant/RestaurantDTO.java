package com.web.mighigankoreancommunity.dto.restaurant;


import com.web.mighigankoreancommunity.entity.Restaurant;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@ToString
@Data
public class RestaurantDTO {
    private Long id;
    private String restaurantName;
    private String restaurantCity;
    private Long ownerId;
    // Save create created time automatically
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Save create update time automatically
    @UpdateTimestamp
    private LocalDateTime updatedAt;



    @Builder
    public RestaurantDTO(long id, String restaurantName, String restaurantCity, Long ownerId) {
    this.id = id;
    this.restaurantName = restaurantName;
    this.restaurantCity = restaurantCity;
    this.ownerId = ownerId;
    }

    public static RestaurantDTO from(Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCity(),
                restaurant.getOwner().getId()
        );
    }


}
