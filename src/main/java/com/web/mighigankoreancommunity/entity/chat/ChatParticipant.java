package com.web.mighigankoreancommunity.entity.chat;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.web.mighigankoreancommunity.domain.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "chatParticipant")
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private ChatRoom room;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

//    two are PK
    private Long userId;
    private String senderName;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
