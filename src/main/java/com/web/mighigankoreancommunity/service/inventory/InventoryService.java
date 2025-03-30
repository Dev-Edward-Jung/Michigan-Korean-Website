package com.web.mighigankoreancommunity.service.inventory;


import com.web.mighigankoreancommunity.dto.InventoryDTO;
import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.inevntory.InventoryRepository;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final RestaurantRepository restaurantRepository;

    // Get Inventory List
    public List<Inventory> getInventoriesByRestaurant(Long retaurantId, Owner loginUser) {
        System.out.println("In Service, loginUser is : " + loginUser.toString());
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(retaurantId, loginUser);
        System.out.println("In Service, restaurantName is : " + restaurant.toString());
        if (restaurant == null) {
            throw new IllegalArgumentException("Invalid restaurant or unauthorized access.");
        }
        return inventoryRepository.findByRestaurantsId(retaurantId);
    }


    public Inventory saveInventory(InventoryDTO dto, Owner member) {
        // 기존 저장 로직
        Inventory inventory = Inventory.builder()
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .unit(dto.getUnit())
                .category(dto.getCategory())
                .restaurant(restaurantRepository.findById(dto.getRestaurantId()).orElseThrow())
                .build();

        return inventoryRepository.save(inventory); // <- Entity 리턴
    }

    public boolean updateInventory(InventoryDTO dto, Owner loginUser) {
        Inventory inventory = inventoryRepository.findById(dto.getId()).orElse(null);
        if (inventory == null) return false;

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId()).orElse(null);
        if (restaurant == null) return false;

        // ✅ 현재 로그인한 Owner가 이 레스토랑의 주인인지 확인
        System.out.println("-------------------------------------------------------- User ID" + loginUser.getId());
        System.out.println("-------------------------------------------------------- User ID" + restaurant.getOwner().getId());
        if (restaurant.getOwner() == null || !restaurant.getOwner().getId().equals(loginUser.getId())) {
            return false; // 소유자가 다르면 거절
        }

        // ✅ 인벤토리가 해당 레스토랑 소속인지도 검증 (추가 안전장치)
        if (!inventory.getRestaurant().getId().equals(restaurant.getId())) {
            return false;
        }

        // 필드 수정
        inventory.setName(dto.getName());
        inventory.setQuantity(dto.getQuantity());
        inventory.setUnit(dto.getUnit());
        inventory.setCategory(dto.getCategory());

        // 저장
        inventoryRepository.save(inventory);

        return true;
    }


    public boolean deleteInventory(InventoryDTO inventoryDTO, Owner loginUser) {
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(inventoryDTO.getRestaurantId(), loginUser);
        System.out.println(restaurant.toString());
        if (restaurant == null) {
            return false;
        }
        Inventory inventory = inventoryRepository.findById(inventoryDTO.getId()).orElse(null);
        System.out.println("-------------------------------" + inventory.toString());
        // 3. 삭제
        inventoryRepository.delete(inventory);
        return true;
    }
}
