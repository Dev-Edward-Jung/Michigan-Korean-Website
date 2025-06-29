package com.web.mighigankoreancommunity.controller.chat;


import com.web.mighigankoreancommunity.dto.ApiResponse;
import com.web.mighigankoreancommunity.dto.chat.ChatRoomDto;
import com.web.mighigankoreancommunity.dto.employee.EmployeeDTO;
import com.web.mighigankoreancommunity.dto.payroll.PayrollResponse;
import com.web.mighigankoreancommunity.entity.chat.ChatMessage;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRestController {

    private final ChatService chatService;

    @MessageMapping("/chat.send")
    @SendTo("/chatroom/{roomId}")
    public ChatMessage send(@DestinationVariable ChatMessage message) {
        return null;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ChatRoomDto>>> fetchEmployee(
            @RequestParam Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        List<ChatRoomDto> chatRoomDtoList = chatService.ChatEmployeeService(restaurantId, customUserDetails);
        return ResponseEntity.ok(ApiResponse.success(chatRoomDtoList, "Fetch Successfully"));
    }

    @GetMapping("/employee/list")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> fetchEmployee(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Long restaurantId
    ) {
        List<EmployeeDTO> employeeDTOList = chatService.getEmployeesForChat(restaurantId, customUserDetails);
        return ResponseEntity.ok(ApiResponse.success(employeeDTOList, "Fetch Successfully"));
    }
}
