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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> saveRestaurant(
            @RequestBody @Parameter(description = "Restaurant data to save") RestaurantDTO dto
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new RuntimeException("Role not found"));


        if ("OWNER".equals(role)) {
            Owner owner = ownerRepository.findOwnerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            restaurantService.saveService(dto, owner);
            // have your service return the saved entity or DTO
            return ResponseEntity.ok(ResponseEntity.status(HttpStatus.OK).build());
        } else  {
            Employee employee = employeeRepository.findEmployeeByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            // analogous for employee…
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        if ("OWNER".equals(role)) {
            Owner owner = ownerRepository.findOwnerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            List<RestaurantDTO> restaurantDTOList = restaurantService.restaurantListService(owner);
            return restaurantDTOList;
        } else if ("EMPLOYEE".equals(role)) {
            Employee employee = employeeRepository.findEmployeeByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            return restaurantService.restaurantListForEmployee(employee);
        } else {
            throw new RuntimeException("Invalid role");
        }
    }
}
