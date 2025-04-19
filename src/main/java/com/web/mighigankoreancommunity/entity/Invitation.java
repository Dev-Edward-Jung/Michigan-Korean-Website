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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token; // 랜덤 토큰 (UUID 등)


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

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Employee employee;


}
