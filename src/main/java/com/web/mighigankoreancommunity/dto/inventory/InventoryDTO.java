package com.web.mighigankoreancommunity.dto.inventory;

import com.web.mighigankoreancommunity.domain.InventoryUnit;
import com.web.mighigankoreancommunity.entity.Inventory;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class InventoryDTO {
    private Long id;

    private String name;

    private Integer quantity;

    private InventoryUnit unit;

    private Long categoryId;

    private String categoryName;

    private Long restaurantId;

    private boolean needNow;


    public static InventoryDTO fromEntity(Inventory entity) {
        return InventoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .quantity(entity.getQuantity())
                .unit(entity.getUnit())
                .needNow(entity.isNeedNow())
                .build();
    }
}


