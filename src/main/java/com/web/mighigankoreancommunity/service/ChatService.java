package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.dto.chat.ChatRoomDto;
import com.web.mighigankoreancommunity.dto.employee.EmployeeDTO;
import com.web.mighigankoreancommunity.dto.employee.RestaurantEmployeeResponse;
import com.web.mighigankoreancommunity.entity.Employee;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import com.web.mighigankoreancommunity.entity.chat.ChatMessage;
import com.web.mighigankoreancommunity.entity.chat.ChatParticipant;
import com.web.mighigankoreancommunity.entity.chat.ChatRoom;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.repository.chat.ChatMessageRepository;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.chat.ChatRoomRepository;
import com.web.mighigankoreancommunity.repository.employee.EmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.RestaurantEmployeeRepository;
import com.web.mighigankoreancommunity.repository.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final EmployeeRepository employeeRepository;
    private final OwnerRepository ownerRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage send(ChatMessage message) {
        return message;
    }

    public List<ChatRoomDto> ChatEmployeeService(
            Long restaurantId,
            CustomUserDetails customUserDetails
    ) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Long userId;
        boolean isExist = false;

        if (customUserDetails.getEmployee() != null) {
            Employee employee = customUserDetails.getEmployee();
            isExist = restaurantEmployeeRepository.existsByEmployee_EmailAndRestaurant_Id(employee.getEmail(), restaurantId);
            userId = employee.getId();
        } else if (customUserDetails.getOwner() != null) {
            Owner owner = customUserDetails.getOwner();
            isExist = ownerRepository.existsByEmail(owner.getEmail());
            userId = owner.getId();
        } else {
            throw new RuntimeException("User is neither owner nor employee");
        }

        if (!isExist) {
            return Collections.emptyList();
        }

        List<ChatRoom> allChatRooms = chatRoomRepository.findChatRoomsByRestaurant_Id(restaurantId);

        List<ChatRoomDto> result = new ArrayList<>();

        for (ChatRoom room : allChatRooms) {
            boolean isParticipant = room.getChatParticipants().stream()
                    .anyMatch(p -> Objects.equals(p.getUserId(), userId));

            if (isParticipant) {
                ChatMessage lastMessage = chatMessageRepository
                        .findTopByRoom_IdOrderByCreatedAtDesc(room.getId())
                        .orElse(new ChatMessage("No messages"));

                ChatRoomDto dto = new ChatRoomDto();
                dto.setId(room.getId());
                dto.setName(room.getName());
                dto.setLastMessage(lastMessage.getMessage());
                result.add(dto);
            }
        }

        return result;
    }


    public List<EmployeeDTO> getEmployeesForChat(Long restaurantId, CustomUserDetails customUserDetails) {

        List<RestaurantEmployee> restaurantEmployees = restaurantEmployeeRepository
                .findRestaurantEmployeesByRestaurant_IdAndApprovedTrue(restaurantId)
                .orElseThrow(() -> new RuntimeException("직원이 없습니다."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("레스토랑이 없습니다."));

        Owner owner = restaurant.getOwner();

        Long currentUserId;
        boolean isOwner = false;

        if (customUserDetails.getOwner() != null) {
            currentUserId = customUserDetails.getOwner().getId();
            isOwner = true;
        } else if (customUserDetails.getEmployee() != null) {
            currentUserId = customUserDetails.getEmployee().getId();
            RestaurantEmployee restaurantEmployee = restaurantEmployeeRepository.findRestaurantEmployeeByEmployee_IdAndRestaurant_Id(currentUserId, restaurantId)
                    .orElseThrow(RuntimeException::new);

//          RestaurantEmployeeId for a restaurant
            currentUserId = restaurantEmployee.getEmployee().getId();

        } else {
            throw new RuntimeException("로그인 정보가 올바르지 않습니다.");
        }

        List<EmployeeDTO> chatMembers = new ArrayList<>();

        if (isOwner) {
            // 오너 → 모든 직원 추가
            for (RestaurantEmployee re : restaurantEmployees) {
                chatMembers.add(new EmployeeDTO(
                        re.getEmployee().getId(),
                        re.getEmployee().getName(),
                        re.getEmployee().getEmail(),
                        re.getMemberRole(),
                        restaurantId
                ));
            }

        } else {
            // 직원 → 오너 추가
                chatMembers.add(new EmployeeDTO(
                        owner.getId(),
                        owner.getOwnerName(),
                        owner.getEmail(),
                        owner.getMemberRole(),
                        restaurantId
                ));

            // 직원 → 다른 직원 추가 (자기 제외)
            for (RestaurantEmployee re : restaurantEmployees) {
                if (!re.getEmployee().getId().equals(currentUserId)) {
                    chatMembers.add(new EmployeeDTO(
                            re.getEmployee().getId(),
                            re.getEmployee().getName(),
                            re.getEmployee().getEmail(),
                            re.getMemberRole(),
                            restaurantId
                    ));
                }
            }
        }

        return chatMembers;
    }
}


