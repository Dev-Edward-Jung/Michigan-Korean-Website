package com.web.mighigankoreancommunity.entity.chat;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.mighigankoreancommunity.domain.RoomType;
import com.web.mighigankoreancommunity.dto.chat.ChatRoomDto;
import com.web.mighigankoreancommunity.entity.Restaurant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Restaurant restaurant;


    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ChatMessage> chatMessages;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<ChatParticipant> chatParticipants;

}
