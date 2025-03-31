package com.web.mighigankoreancommunity.entity;

import com.web.mighigankoreancommunity.domain.SubscriptionStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "subscription")
@Entity
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
