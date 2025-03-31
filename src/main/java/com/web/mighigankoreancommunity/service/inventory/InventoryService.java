package com.web.mighigankoreancommunity.service.inventory;


import com.web.mighigankoreancommunity.dto.InventoryDTO;
import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.inevntory.CategoryRepository;
import com.web.mighigankoreancommunity.repository.inevntory.InventoryRepository;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    // Get Inventory List
    public List<InventoryDTO> getInventoriesByRestaurant(Long retaurantId, Owner loginUser) {
        System.out.println("In Service, loginUser is : " + loginUser.toString());
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(retaurantId, loginUser);
        System.out.println("In Service, restaurantName is : " + restaurant.toString());
        if (restaurant == null) {
            throw new IllegalArgumentException("Invalid restaurant or unauthorized access.");
        }
        List<Inventory> inventories = inventoryRepository.findByRestaurantsId(retaurantId);
        List<InventoryDTO> inventoryDTOList = new ArrayList<>();
        inventories.forEach(inventory -> {
            InventoryDTO dto = new InventoryDTO();
            dto.setId(inventory.getId());
            dto.setName(inventory.getName());
            dto.setRestaurantId(inventory.getRestaurant().getId());
            dto.setQuantity(inventory.getQuantity());
            dto.setCategoryId(inventory.getCategory().getId());
            dto.setCategoryName(inventory.getCategory().getName());
            dto.setUnit(inventory.getUnit());
            inventoryDTOList.add(dto);
        });
        return inventoryDTOList;
    }


    public Inventory saveInventory(InventoryDTO dto, Owner member) {
        // ************* 카테고리 저장 만들어야함
        Inventory inventory = Inventory.builder()
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .unit(dto.getUnit())
                .restaurant(restaurantRepository.findById(dto.getRestaurantId()).orElseThrow())
                .category(categoryRepository.findById(dto.getCategoryId()).orElseThrow())
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

        // 필드 수정 ***************카테고리까지 set해줘야함
        inventory.setName(dto.getName());
        inventory.setQuantity(dto.getQuantity());
        inventory.setUnit(dto.getUnit());

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
