package com.web.mighigankoreancommunity.controller.inventory;


import com.web.mighigankoreancommunity.domain.InventoryUnit;
import com.web.mighigankoreancommunity.dto.ApiResponse;
import com.web.mighigankoreancommunity.dto.category.CategoryDTO;
import com.web.mighigankoreancommunity.dto.inventory.InventoryDTO;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.inventory.CategoryService;
import com.web.mighigankoreancommunity.service.inventory.InventoryService;
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
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "APIs for managing inventory items and categories")
public class InventoryRestController {

    private final InventoryService inventoryService;
    private final CategoryService categoryService;

    @Operation(summary = "Create inventory item", description = "Adds a new inventory item to the restaurant.")
    @PostMapping("/save")
    public ResponseEntity<?> saveInventory(
            @RequestBody @Parameter(description = "Inventory item data") InventoryDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        System.out.println(userDetails.getOwner().getEmail());
        Long inventoryId = inventoryService.saveInventory(dto, userDetails);
        // Note: conversion from InventoryDTO to CategoryDTO handled internally
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse());
    }

    @Operation(summary = "Update inventory item", description = "Updates an existing inventory item.")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Long>> updateInventory(
            @RequestBody @Parameter(description = "Updated inventory item data") InventoryDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long updatedId = inventoryService.updateInventory(dto, userDetails);
        return ResponseEntity.ok(
                ApiResponse.success(updatedId, "Update Successfully")
        );
    }

    @Operation(summary = "Delete inventory item", description = "Deletes the given inventory item.")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteInventory(
            @RequestBody @Parameter(description = "Inventory item to delete") InventoryDTO dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        inventoryService.deleteInventory(dto, customUserDetails);
        return ResponseEntity.ok(
                ApiResponse.success(1, "Delete Successfully")
        );
    }

    @Operation(summary = "Get inventory and category lists", description = "Returns all inventory items and their categories for a given restaurant.")
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> inventoryList(
            @Parameter(description = "Restaurant ID") @RequestParam Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<InventoryDTO> inventoryList = inventoryService.getInventoriesByRestaurant(restaurantId, customUserDetails);
        List<CategoryDTO> categoryList = categoryService.findCategoriesByRestaurant(restaurantId, customUserDetails);

        Map<String, Object> result = new HashMap<>();
        result.put("categoryList", categoryList);
        result.put("inventoryList", inventoryList);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get inventory unit list", description = "Returns a list of available inventory units (e.g., PIECE, GRAM, LITER).")
    @GetMapping("/unit/list")
    public ResponseEntity<List<String>> unitList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<String> unitList = Arrays.stream(InventoryUnit.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(unitList);
    }
}
