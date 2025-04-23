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
@ToString
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String email;

    private String phone;

    private String password;

    // Save create created time automatically
    @CreationTimestamp
    private LocalDateTime joined_at;

    // Save create update time automatically
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RestaurantEmployee> restaurantEmployeeList;


    @OneToOne(fetch = FetchType.LAZY)
    private Invitation invitation;

    public Employee(String name, String email, Invitation invitation) {
        this.name = name;
        this.email = email;
        this.invitation = invitation;

    }

}
