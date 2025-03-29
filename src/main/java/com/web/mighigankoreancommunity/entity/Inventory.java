package com.web.mighigankoreancommunity.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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

    private String unit; // ex: "boxes", "bags"

    private String category;

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

    public Inventory(String name, Integer quantity, String unit, Restaurant restaurant) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.restaurant = restaurant;
    }
}
