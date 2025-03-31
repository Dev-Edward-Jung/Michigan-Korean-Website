package com.web.mighigankoreancommunity.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.mighigankoreancommunity.domain.MemberRole;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonManagedReference
    private List<RestaurantEmployee> restaurantEmployeeList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Owner member;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole = MemberRole.EMPLOYEE; // ex: manager, owner, kitchen, sushi

}
