package com.web.mighigankoreancommunity.entity;

import com.web.mighigankoreancommunity.domain.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "subscription")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long stripe_subscription_id;
    private SubscriptionStatus status;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
