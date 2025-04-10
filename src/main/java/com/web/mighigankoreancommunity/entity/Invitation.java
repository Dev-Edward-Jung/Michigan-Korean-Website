package com.web.mighigankoreancommunity.entity;


import com.web.mighigankoreancommunity.domain.InvitationStatus;
import com.web.mighigankoreancommunity.domain.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation")
@Getter
@Setter
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String token; // 랜덤 토큰 (UUID 등)

    @Enumerated(EnumType.STRING)
    private MemberRole role; // 예: MANAGER or KITCHEN

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    private boolean used; // 이미 사용된 초대인지 확인

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime sentAt;


    private LocalDateTime expiresAt;

    private Long invitedBy;


    @Enumerated(EnumType.STRING)
    private InvitationStatus status = InvitationStatus.PENDING;

}
