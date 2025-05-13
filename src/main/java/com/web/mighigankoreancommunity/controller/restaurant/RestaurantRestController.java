package com.web.mighigankoreancommunity.controller.restaurant;



import com.web.mighigankoreancommunity.dto.restaurant.RestaurantDTO;
import com.web.mighigankoreancommunity.entity.Employee;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.repository.employee.EmployeeRepository;
import com.web.mighigankoreancommunity.repository.owner.OwnerRepository;
import com.web.mighigankoreancommunity.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "APIs for managing restaurants")
public class RestaurantRestController {

    private final RestaurantService restaurantService;
    private final OwnerRepository ownerRepository;
    private final EmployeeRepository employeeRepository;

    @Operation(summary = "Create a new restaurant", description = "Creates a new restaurant for the currently logged-in owner.")
    @PostMapping("/save")
    public void saveRestaurant(
            @RequestBody @Parameter(description = "Restaurant data to save") RestaurantDTO dto
    ) {
        System.out.println("Restaurant data to save: " + dto.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            Owner owner = customUserDetails.getOwner(); // 또는 getUsername() 등
            restaurantService.saveService(dto, owner);
        } else {
            throw new RuntimeException("Unauthorized or invalid token.");
        }
    }

    @Operation(summary = "Get list of restaurants", description = "Returns a list of restaurants owned by the user (owner) or assigned to them (employee).")
    @GetMapping("/list")
    @ResponseBody
    public List<RestaurantDTO> restaurantList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String email = authentication.getName(); // JwtTokenProvider에서 저장한 이메일
        System.out.println("When you get list of restaurant Email : " + email);
        System.out.println("When you get list of restaurant credential : " + authentication.getCredentials());
        System.out.println("When you get list of restaurant authorities : " + authentication.getAuthorities());
        System.out.println("When you get list of restaurant details : " + authentication.getDetails());
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        System.out.println("When you get list of restaurant role : " + role);
        if ("OWNER".equals(role)) {
            Owner owner = ownerRepository.findOwnerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            return restaurantService.restaurantListService(owner);
        } else if ("EMPLOYEE".equals(role)) {
            Employee employee = employeeRepository.findEmployeeByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            return restaurantService.restaurantListForEmployee(employee);
        } else {
            throw new RuntimeException("Invalid role");
        }
    }
}
