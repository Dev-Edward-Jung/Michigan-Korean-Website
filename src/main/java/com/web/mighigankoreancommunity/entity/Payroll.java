package com.web.mighigankoreancommunity.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;




@Entity
@Table(name = "payroll")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyWage;

    private LocalDate payDate;

    // Save create created time automatically
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Save create update time automatically
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private RestaurantEmployee restaurantEmployee;

    public Payroll(BigDecimal hourlyWage, RestaurantEmployee restaurantEmployee) {
        this.hourlyWage = hourlyWage;
        this.restaurantEmployee = restaurantEmployee;
    }
}
