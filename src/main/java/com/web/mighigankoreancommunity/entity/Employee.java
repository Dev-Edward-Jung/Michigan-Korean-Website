package com.web.mighigankoreancommunity.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phone;

    private boolean approved = false; // 사장님이 승인했는지 여부

    // Save create created time automatically
    @CreationTimestamp
    private LocalDateTime joined_at;

    // Save create update time automatically
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonBackReference
    private List<RestaurantEmployee> restaurantEmployeeList;

    @ManyToOne(fetch = FetchType.LAZY)
    private Owner member;

    @ManyToOne(fetch = FetchType.LAZY)
    private RoleType roleType; // ex: manager, chef, staff

}
