package com.web.mighigankoreancommunity.entity;


import com.web.mighigankoreancommunity.domain.MemberRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inviteToken")
public class InviteToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code; // 랜덤 토큰 (UUID 등)

    @Enumerated(EnumType.STRING)
    private MemberRole invitedRole; // 예: MANAGER or KITCHEN

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    private boolean used; // 이미 사용된 초대인지 확인

    private LocalDateTime createdAt;

}
