package com.web.mighigankoreancommunity.controller.inventory;


import com.web.mighigankoreancommunity.domain.InventoryUnit;
import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.dto.ApiResponse;
import com.web.mighigankoreancommunity.dto.CategoryDTO;
import com.web.mighigankoreancommunity.dto.InventoryDTO;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.inventory.CategoryService;
import com.web.mighigankoreancommunity.service.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
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
        Long inventoryId = inventoryService.saveInventory(dto, userDetails);
//        CategoryDTO 변환 메소드 필요
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Long>> updateInventory(@RequestBody InventoryDTO dto,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long updatedId = inventoryService.updateInventory(dto, userDetails);

        return ResponseEntity.ok(
                ApiResponse.success(updatedId, "Update Successfully")
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteInventory(@RequestBody InventoryDTO dto,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        inventoryService.deleteInventory(dto, customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(1, "Update Successfully")
        );
    }


    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> inventoryList(@RequestParam Long restaurantId,
                                                             @AuthenticationPrincipal CustomUserDetails customUserDetails){

        List<InventoryDTO> inventoryList = inventoryService.getInventoriesByRestaurant(restaurantId, customUserDetails);
        List<CategoryDTO> categoryList = categoryService.findCategoriesByRestaurant(restaurantId, customUserDetails);


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
