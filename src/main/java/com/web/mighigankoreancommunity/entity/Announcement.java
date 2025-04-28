package com.web.mighigankoreancommunity.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.mighigankoreancommunity.domain.ContentType;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "announcement")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    private ContentType type = ContentType.NORMAL;

    private boolean deleted;

    @ManyToOne
    @JsonBackReference
    private RestaurantEmployee employee;

    // Save create created time automatically
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Save create update time automatically
    @UpdateTimestamp
    private LocalDateTime updatedAt;


//
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

//    if owner wrote announcement
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

//    if an employee wrote announcement
    @ManyToOne
    @JoinColumn(name = "restaurant_employee_id")
    private RestaurantEmployee restaurantEmployee;


    public static Announcement create(String content,String title, ContentType type, Restaurant restaurant,
                                      Owner owner, RestaurantEmployee restaurantEmployee) {
        Announcement announcement = new Announcement();
        announcement.content = content;
        announcement.title = title;
        announcement.type = type;
        announcement.restaurant = restaurant;
        announcement.owner = owner;
        announcement.restaurantEmployee = restaurantEmployee;
        announcement.createdAt = LocalDateTime.now();
        return announcement;
    }

    // 수정용 메소드
    public void update(String content, ContentType type) {
        this.content = content;
        this.type = type;
        // restaurant, owner, employee는 수정 안한다고 가정
    }
}
