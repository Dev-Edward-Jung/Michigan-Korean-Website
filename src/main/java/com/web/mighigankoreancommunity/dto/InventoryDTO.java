package com.web.mighigankoreancommunity.dto;

import com.web.mighigankoreancommunity.domain.InventoryUnit;
import com.web.mighigankoreancommunity.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InventoryDTO {
    private Long id;

    private String name;

    private Integer quantity;

    private InventoryUnit unit;

    private Long categoryId;

    private String categoryName;

    private Long restaurantId;


    public static InventoryDTO fromEntity(Inventory entity) {
        return InventoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .quantity(entity.getQuantity())
                .unit(entity.getUnit())
                .build();
    }
}


