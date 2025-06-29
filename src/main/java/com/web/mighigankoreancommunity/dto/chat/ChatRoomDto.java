package com.web.mighigankoreancommunity.dto.chat;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
    private Long id;
    private String name;
    private String lastMessage;
    private List<ChatParticipantsDto> chatParticipants;
    private String hours;
}
