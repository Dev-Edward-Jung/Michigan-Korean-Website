package com.web.mighigankoreancommunity.entity;


import com.web.mighigankoreancommunity.domain.MemberRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class RestaurantEmployee {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    Restaurant restaurant;

    @ManyToOne
    Employee employee;

    private MemberRole memberRole;
    private LocalDateTime startDate;
    private boolean active;
}
