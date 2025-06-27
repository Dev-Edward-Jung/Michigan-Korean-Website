package com.web.mighigankoreancommunity.dto.chat;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private Long messageId;      // 메시지 DB 저장 ID
    private Long roomId;         // 채팅방 ID
    private Long senderId;       // 보낸 사람 ID
    private String senderName;   // 보낸 사람 이름 (옵션)
    private String message;      // 메시지 내용
    private String type;         // "TALK", "ENTER", "LEAVE"
}
