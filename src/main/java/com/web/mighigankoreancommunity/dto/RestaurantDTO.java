package com.web.mighigankoreancommunity.dto;


import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Data
public class RestaurantDTO {
    private long id;
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


}
