package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.dto.InventoryDTO;
import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.InventoryRepository;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final RestaurantRepository restaurantRepository;

    // ✅ 인벤토리 리스트 가져오기
    public List<Inventory> getInventoriesByRestaurant(Long restaurantId, Member loginUser) {
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(restaurantId, loginUser);
        System.out.println("In Service, restaurantName is : " + restaurant.getName());
        if (restaurant == null) {
            throw new IllegalArgumentException("Invalid restaurant or unauthorized access.");
        }
        return inventoryRepository.findByRestaurantsId(restaurantId);
    }

    // ✅ 인벤토리 추가
    public boolean saveInventory(InventoryDTO dto, Member loginUser) {
        try {
            Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(dto.getRestaurantId(), loginUser);
            if (restaurant == null) return false;

            Inventory inventory = new Inventory(dto.getName(), dto.getQuantity(), dto.getUnit(), restaurant);
            inventoryRepository.save(inventory);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ 인벤토리 수정
    public boolean updateInventory(InventoryDTO dto, Member loginUser) {
        try {
            Inventory inventory = inventoryRepository.findById(dto.getId()).orElse(null);
            if (inventory == null || !inventory.getRestaurant().getOwner().getId().equals(loginUser.getId())) {
                return false;
            }
            inventory.setName(dto.getName());
            inventory.setQuantity(dto.getQuantity());
            inventory.setUnit(dto.getUnit());
            inventoryRepository.save(inventory);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
