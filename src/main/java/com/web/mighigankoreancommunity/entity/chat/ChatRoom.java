package com.web.mighigankoreancommunity.entity.chat;


import com.web.mighigankoreancommunity.domain.RoomType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "chatRoom")
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

//    Name of chat room
    private String name;

    private LocalDateTime createdAt;
}
