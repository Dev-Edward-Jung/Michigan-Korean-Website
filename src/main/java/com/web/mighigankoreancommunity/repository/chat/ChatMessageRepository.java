package com.web.mighigankoreancommunity.repository.chat;


import com.web.mighigankoreancommunity.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    public Optional<ChatMessage> findChatMessageBySenderId(Long senderId);

    Optional<ChatMessage> findChatMessageByRoom_Id(Long roomId);

    Optional<ChatMessage> findTopByRoom_IdOrderByCreatedAtDesc(Long roomId);
}
