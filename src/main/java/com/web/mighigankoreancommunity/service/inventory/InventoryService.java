package com.web.mighigankoreancommunity.service.inventory;


import com.web.mighigankoreancommunity.dto.InventoryDTO;
import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.error.*;
import com.web.mighigankoreancommunity.repository.inevntory.CategoryRepository;
import com.web.mighigankoreancommunity.repository.inevntory.InventoryRepository;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    // Get Inventory List
    public List<InventoryDTO> getInventoriesByRestaurant(Long retaurantId, Owner loginUser) {
        System.out.println("In Service, loginUser is : " + loginUser.toString());
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(retaurantId, loginUser)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found."));
        System.out.println("In Service, restaurantName is : " + restaurant.toString());
        if (restaurant == null) {
            throw new RestaurantNotFoundException("Restaurant not found.");
        }
        List<Inventory> inventories = inventoryRepository.findByRestaurantsId(retaurantId)
                .orElseThrow(()-> new RuntimeException("Inventory not found."));


        return inventories.stream().map(inventory ->
                new InventoryDTO(
                        inventory.getId(),
                        inventory.getName(),
                        inventory.getQuantity(),
                        inventory.getUnit(),
                        inventory.getCategory().getId(),
                        inventory.getCategory().getName(),
                        inventory.getRestaurant().getId()
                )).collect(Collectors.toList());
    }


    @Transactional
    public Long saveInventory(InventoryDTO dto, Owner owner) {
        // ✅ 레스토랑 권한 검증
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .filter(r -> r.getOwner().getId().equals(owner.getId()))
                .orElseThrow(() -> new UnauthorizedRestaurantAccessException("You are NOT authorized to make this inventory"));

        // ✅ 카테고리 존재 여부 확인
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        // ✅ 중복 이름 검사 (선택 사항)
        boolean exists = inventoryRepository.existsByNameAndRestaurant(dto.getName(), restaurant);
        if (exists) {
            throw new DuplicateInventoryException("You have already this in your inventory.");
        }

        // ✅ DTO → Entity 매핑 (직접 하지 않도록 Mapper 분리 가능)
        Inventory inventory = Inventory.builder()
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .unit(dto.getUnit())
                .restaurant(restaurant)
                .category(category)
                .build();

        Inventory saved = inventoryRepository.save(inventory);
        return saved.getId();
    }



    @Transactional
    public Long updateInventory(InventoryDTO dto, Owner loginUser) {
        // Search Inventory
        Inventory inventory = inventoryRepository.findById(dto.getId())
                .orElseThrow(InventoryNotFoundException::new);

        // Confirm if the user is authorized
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(UnauthorizedRestaurantAccessException::new);

        if (!restaurant.getOwner().getId().equals(loginUser.getId())) {
            throw new UnauthorizedRestaurantAccessException("Not Authorized user to make this inventory");
        }

        // 🔍 3. 인벤토리가 해당 레스토랑 소속인지 확인
        if (!inventory.getRestaurant().getId().equals(restaurant.getId())) {
            throw new IllegalStateException("Inventory does not belong to the restaurant.");
        }

        // 🔍 4. 카테고리 조회 및 설정
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        // 🔧 5. 필드 업데이트
        inventory.setName(dto.getName());
        inventory.setQuantity(dto.getQuantity());
        inventory.setUnit(dto.getUnit());
        inventory.setCategory(category);

        // 💾 6. 저장
        Inventory updated = inventoryRepository.save(inventory);

        return updated.getId(); // ✅ 변경된 인벤토리 ID 반환
    }


    @Transactional
    public void deleteInventory(InventoryDTO inventoryDTO, Owner loginUser) {
        // 1. 레스토랑 소유자 검증
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(
                inventoryDTO.getRestaurantId(), loginUser
        ).orElseThrow(UnauthorizedRestaurantAccessException::new);

        // 2. 인벤토리 존재 여부 확인
        Inventory inventory = inventoryRepository.findById(inventoryDTO.getId())
                .orElseThrow(InventoryNotFoundException::new);

        // 3. 인벤토리가 해당 레스토랑 소속인지 확인 (추가 안전장치)
        if (!inventory.getRestaurant().getId().equals(restaurant.getId())) {
            throw new IllegalStateException("Inventory does not belong to the restaurant.");
        }

        // 4. 삭제
        inventoryRepository.delete(inventory);
    }
}
