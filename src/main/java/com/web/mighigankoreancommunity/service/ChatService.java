package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.dto.employee.EmployeeDTO;
import com.web.mighigankoreancommunity.entity.Employee;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.entity.chat.ChatMessage;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.employee.EmployeeRepository;
import com.web.mighigankoreancommunity.repository.employee.RestaurantEmployeeRepository;
import com.web.mighigankoreancommunity.repository.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final EmployeeRepository employeeRepository;
    private final OwnerRepository ownerRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;

    public ChatMessage send(ChatMessage message) {
        return message;
    }

    public EmployeeDTO ChatEmployeeService(
            Long restaurantId,
            CustomUserDetails customUserDetails
    ) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found"));
        boolean isExist = false;
        if (customUserDetails.getEmployee() != null) {
            String employeeEmail = customUserDetails.getEmployee().getEmail();
            isExist = restaurantEmployeeRepository.existsByEmployee_EmailAndRestaurant_Id(employeeEmail, restaurantId);
        } else if (customUserDetails.getOwner() != null) {
            isExist = ownerRepository.existsByEmail(customUserDetails.getOwner().getEmail());
        } else {
            throw new RuntimeException("It is not owner or employee");
        }


        return new EmployeeDTO();
    }

}
