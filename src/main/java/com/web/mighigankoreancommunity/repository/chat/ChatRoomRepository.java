package com.web.mighigankoreancommunity.repository.chat;


import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findChatRoomsByRestaurant_Id(Long restaurantId);
}
