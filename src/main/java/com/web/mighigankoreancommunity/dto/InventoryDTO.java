package com.web.mighigankoreancommunity.dto;

import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.entity.Owner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InventoryDTO {
    private Long id;

    private String name;

    private Integer quantity;

    private String unit;

    private String category;


    private Long restaurantId;
    public static InventoryDTO fromEntity(Inventory entity) {
        return InventoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .quantity(entity.getQuantity())
                .unit(entity.getUnit())
                .category(entity.getCategory())
                .build();
    }
}


