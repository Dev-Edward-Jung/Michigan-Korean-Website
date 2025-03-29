package com.web.mighigankoreancommunity.controller.inventory;


import com.web.mighigankoreancommunity.dto.InventoryDTO;
import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.repository.member.MemberRepository;
import com.web.mighigankoreancommunity.service.InventoryService;
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
    public ResponseEntity<?> saveInventory(@RequestBody InventoryDTO dto,
                                           @AuthenticationPrincipal Member member) {
        boolean result = inventoryService.saveInventory(dto, member);

        if (result) {
            return ResponseEntity.ok("Inventory saved successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to save inventory");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateInventory(@RequestBody InventoryDTO dto,
                                             @AuthenticationPrincipal Member loginUser) {
        boolean updated = inventoryService.updateInventory(dto, loginUser);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<Inventory>> inventoryList(@RequestParam("restaurantId") Long restaurantId,
                                                         @AuthenticationPrincipal Member member) {
        List<Inventory> inventoryList = inventoryService.getInventoriesByRestaurant(restaurantId, member);
        return ResponseEntity.ok(inventoryList);
    }



}
