package com.web.mighigankoreancommunity.entity.chat;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private ChatRoom room;

    @Column(columnDefinition = "TEXT")
    private String message;

    private Long senderId;
    private String senderEmail;

    @Enumerated(EnumType.STRING)
    private MessageType ChatType;

    private String mediaURL;

    private boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public ChatMessage(String message) {
        this.message = message;
    }
}
