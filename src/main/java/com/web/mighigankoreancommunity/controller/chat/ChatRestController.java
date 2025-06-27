package com.web.mighigankoreancommunity.controller.chat;


import com.web.mighigankoreancommunity.dto.ApiResponse;
import com.web.mighigankoreancommunity.dto.employee.EmployeeDTO;
import com.web.mighigankoreancommunity.entity.chat.ChatMessage;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @MessageMapping("/chat.send")
    @SendTo("/chatroom/{roomId}")
    public ChatMessage send(@DestinationVariable ChatMessage message) {
        return null;
    }

    @RequestMapping("/employee/list")
    public ApiResponse<ResponseEntity<EmployeeDTO>> fetchEmployee(
            @RequestParam Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {


        return null;
    }
}
