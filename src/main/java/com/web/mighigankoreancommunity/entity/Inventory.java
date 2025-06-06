package com.web.mighigankoreancommunity.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.web.mighigankoreancommunity.domain.InventoryUnit;
import com.web.mighigankoreancommunity.dto.inventory.InventoryDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Inventory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private InventoryUnit unit; // ex: "boxes", "bags"

    private boolean needNow = false;

    // Save create created time automatically
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Save create update time automatically
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;


    public static Inventory from (InventoryDTO inventoryDTO) {
        return Inventory.builder()
                .id(inventoryDTO.getId())
                .name(inventoryDTO.getName())
                .quantity(inventoryDTO.getQuantity())
                .unit(inventoryDTO.getUnit())
                .needNow(inventoryDTO.isNeedNow())
                .build();
    }

}
