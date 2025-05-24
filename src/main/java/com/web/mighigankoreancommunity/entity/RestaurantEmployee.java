package com.web.mighigankoreancommunity.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.mighigankoreancommunity.domain.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "restaurantEmployee")
@Builder
public class RestaurantEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
            @JsonBackReference
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
            @JsonBackReference
    private Employee employee;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Payroll payroll;

//    consider if employee has different role in different restaurant
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole = MemberRole.EMPLOYEE; // ex: manager, owner, kitchen, sushi

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurantEmployee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Schedule> schedules;

    @Column(name = "approved", nullable = false)
    private boolean approved = false;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "restaurantEmployee", orphanRemoval = true)
            @JsonManagedReference
    List<Announcement> announcementList;
}
