package com.web.mighigankoreancommunity.entity;

import com.web.mighigankoreancommunity.domain.SubscriptionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Subscription {
    @Id
    @GeneratedValue
    private Long id;
    private Long stripe_subscription_id;
    private SubscriptionStatus status;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
