package com.web.mighigankoreancommunity.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.web.mighigankoreancommunity.domain.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "restaurantEmployee")
public class RestaurantEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
            @JsonBackReference
    Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
            @JsonBackReference
    Employee employee;

    private MemberRole memberRole;
    private LocalDateTime startDate;
    private boolean active;
}
