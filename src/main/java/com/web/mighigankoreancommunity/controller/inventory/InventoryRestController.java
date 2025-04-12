package com.web.mighigankoreancommunity.controller.inventory;


import com.web.mighigankoreancommunity.domain.InventoryUnit;
import com.web.mighigankoreancommunity.dto.CategoryDTO;
import com.web.mighigankoreancommunity.dto.InventoryDTO;
import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.CustomUserDetails;
import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.service.inventory.CategoryService;
import com.web.mighigankoreancommunity.service.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/inventory")
@RestController
public class InventoryRestController {
    private final InventoryService inventoryService;
    private final CategoryService categoryService;

    @PostMapping("/save")
    public void saveInventory(@RequestBody InventoryDTO dto,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        Inventory savedInventory = inventoryService.saveInventory(dto, userDetails.getOwner());
//        CategoryDTO 변환 메소드 필요
        InventoryDTO responseDto = InventoryDTO.fromEntity(savedInventory); // <- DTO 변환 메서드 필요
        System.out.println("response DTO : " + responseDto);

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateInventory(@RequestBody InventoryDTO dto,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean updated = inventoryService.updateInventory(dto, userDetails.getOwner());
        System.out.println(updated);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteInventory(@RequestBody InventoryDTO dto,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        boolean result = inventoryService.deleteInventory(dto, customUserDetails.getOwner());
        return result ? ResponseEntity.ok("Deleted successfully") :
                ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed");
    }


    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> inventoryList(@RequestParam Long restaurantId,
                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        List<InventoryDTO> inventoryList = inventoryService.getInventoriesByRestaurant(restaurantId, customUserDetails.getOwner());
        List<CategoryDTO> categoryList = categoryService.findCategoriesByRestaurant(restaurantId, customUserDetails.getOwner());

        Map<String, Object> result = new HashMap<>();
        result.put("categoryList", categoryList);
        result.put("inventoryList", inventoryList);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/unit/list")
    public ResponseEntity<List<String>> unitList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<String> unitList = Arrays.stream(InventoryUnit.values()).map(Enum::name).collect(Collectors.toList());
        return ResponseEntity.ok(unitList);

    }



}
