package com.web.mighigankoreancommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryDTO {
    private Long id;

    private String name;

    private Integer quantity;

    private String unit;

    private Long restaurantId;


}
