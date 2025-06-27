package com.web.mighigankoreancommunity.entity.chat;


import com.web.mighigankoreancommunity.domain.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "checkReadStatus")
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CheckReadStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatMessage message;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private Long userId;

    @CreationTimestamp
    private LocalDateTime readAt;

}
