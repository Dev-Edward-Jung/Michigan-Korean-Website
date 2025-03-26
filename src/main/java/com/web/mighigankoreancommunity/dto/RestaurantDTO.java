package com.web.mighigankoreancommunity.dto;


import com.web.mighigankoreancommunity.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RestaurantDTO {
    private long id;
    private String name;
    private String city;
}
