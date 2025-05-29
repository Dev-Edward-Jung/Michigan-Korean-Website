package com.web.mighigankoreancommunity.service.inventory;


import com.web.mighigankoreancommunity.dto.inventory.InventoryDTO;
import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.error.*;
import com.web.mighigankoreancommunity.repository.inevntory.CategoryRepository;
import com.web.mighigankoreancommunity.repository.inevntory.InventoryRepository;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    // Get Inventory List

    public Page<InventoryDTO> getPagedInventoriesByRestaurant(Long restaurantId, CustomUserDetails loginUser, Pageable pageable) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found."));
        System.out.println("지금 들어온 아이디는 " + restaurantId);

        if (!(loginUser.isOwner() || loginUser.isEmployee())) {
            throw new UnauthorizedRestaurantAccessException("Login required");
        }

        Page<Inventory> page = inventoryRepository.findByRestaurantIdPaged(restaurantId, pageable);
        System.out.println("????"  + page.getContent().get(0).getName());

        return page.map(inventory -> new InventoryDTO(
                inventory.getId(),
                inventory.getName(),
                inventory.getQuantity(),
                inventory.getUnit(),
                inventory.getCategory().getId(),
                inventory.getCategory().getName(),
                inventory.getRestaurant().getId(),
                inventory.isNeedNow()
        ));
    }


    @Transactional
    public InventoryDTO saveInventory(InventoryDTO dto, CustomUserDetails loginUser) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new UnauthorizedRestaurantAccessException("Restaurant not found"));

        if (!hasPermission(restaurant, loginUser)) {
            throw new UnauthorizedRestaurantAccessException("Not authorized to save inventory");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        boolean exists = inventoryRepository.existsByNameAndRestaurant(dto.getName(), restaurant);
        if (exists) {
            throw new DuplicateInventoryException("You already have this item in your inventory.");
        }

        Inventory inventory = Inventory.from(dto);
        inventory.setRestaurant(restaurant);
        inventory.setCategory(category);
        try{
            Inventory saved = inventoryRepository.save(inventory);
            InventoryDTO inventoryDTO = InventoryDTO.fromEntity(saved);
            inventoryDTO.setRestaurantId(restaurant.getId());
            inventoryDTO.setCategoryId(category.getId());
            inventoryDTO.setCategoryName(category.getName());
            inventoryDTO.setUnit(dto.getUnit());

            return inventoryDTO;
        } catch (Exception e){
            throw new RuntimeException("Save inventory failed");
        }


    }



    @Transactional
    public InventoryDTO updateInventory(InventoryDTO dto, CustomUserDetails loginUser) {
        Inventory inventory = inventoryRepository.findById(dto.getId())
                .orElseThrow(InventoryNotFoundException::new);

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(UnauthorizedRestaurantAccessException::new);

        if (!hasPermission(restaurant, loginUser)) {
            throw new UnauthorizedRestaurantAccessException("Not authorized to update this inventory");
        }

        if (!inventory.getRestaurant().getId().equals(restaurant.getId())) {
            throw new IllegalStateException("Inventory does not belong to the restaurant.");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);



        inventory.setName(dto.getName());
        inventory.setQuantity(dto.getQuantity());
        inventory.setUnit(dto.getUnit());
        inventory.setCategory(category);
        inventory.setNeedNow(dto.isNeedNow());

        Inventory savedEntity = inventoryRepository.save(inventory);
        InventoryDTO inventoryDTO = InventoryDTO.fromEntity(savedEntity);
        inventoryDTO.setRestaurantId(restaurant.getId());
        inventoryDTO.setCategoryId(category.getId());
        inventoryDTO.setCategoryName(category.getName());

        return inventoryDTO;
    }



    @Transactional
    public Long deleteInventory(InventoryDTO dto, CustomUserDetails loginUser) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(UnauthorizedRestaurantAccessException::new);

        Inventory inventory = inventoryRepository.findById(dto.getId())
                .orElseThrow(InventoryNotFoundException::new);

        if (!hasPermission(restaurant, loginUser)) {
            throw new UnauthorizedRestaurantAccessException("Not authorized to delete this inventory");
        }

        if (!inventory.getRestaurant().getId().equals(restaurant.getId())) {
            throw new IllegalStateException("Inventory does not belong to the restaurant.");
        }

        inventoryRepository.delete(inventory);
        return inventory.getId();
    }


    private boolean hasPermission(Restaurant restaurant, CustomUserDetails loginUser) {
        if (loginUser.isOwner()) {
            return restaurant.getOwner().getId().equals(loginUser.getOwner().getId());
        } else if (loginUser.isEmployee()) {
            return loginUser.getRestaurantEmployee() != null &&
                    loginUser.getRestaurantEmployee().getRestaurant().getId().equals(restaurant.getId());
        }
        return false;
    }
}
