package com.web.mighigankoreancommunity.entity.chat;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.domain.MessageType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "chatMessage")
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom room;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType ChatType;

    private String mediaURL;

    @Enumerated(EnumType.STRING)
    private MemberRole senderRole;

    private String senderName;

    private Long senderId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
