package com.web.mighigankoreancommunity.controller.inventory;


import com.web.mighigankoreancommunity.dto.InventoryDTO;
import com.web.mighigankoreancommunity.entity.CustomUserDetails;
import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.service.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/inventory")
@RestController
public class InventoryRestController {
    private final InventoryService inventoryService;

    @PostMapping("/save")
    public ResponseEntity<InventoryDTO> saveInventory(@RequestBody InventoryDTO dto,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        Inventory savedInventory = inventoryService.saveInventory(dto, userDetails.getOwner());
        InventoryDTO responseDto = InventoryDTO.fromEntity(savedInventory); // <- DTO 변환 메서드 필요
        System.out.println("response DTO : " + responseDto);
        return ResponseEntity.ok(responseDto);
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
    public ResponseEntity<List<Inventory>> inventoryList(@RequestParam Long restaurantId,
                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        System.out.println(customUserDetails.toString());

        List<Inventory> inventoryList = inventoryService.getInventoriesByRestaurant(restaurantId, customUserDetails.getOwner());
        return ResponseEntity.ok(inventoryList);
    }



}
