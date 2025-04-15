package com.web.mighigankoreancommunity.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.mighigankoreancommunity.domain.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Getter
@Setter
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

//    consider if employee has different role in different restaurant
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole = MemberRole.EMPLOYEE; // ex: manager, owner, kitchen, sushi

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurantEmployee")
    @JsonManagedReference
    List<Schedule> schedules;

    private LocalDateTime startDate;

    private boolean active;
}
