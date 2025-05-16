package com.web.mighigankoreancommunity.controller.inventory;


import com.web.mighigankoreancommunity.dto.category.CategoryDTO;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.inventory.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Tag(name = "Inventory Category", description = "APIs for managing inventory categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    @Operation(summary = "Get category list", description = "Retrieves all inventory categories for the given restaurant.")
    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> findCategories(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "Restaurant ID") @RequestParam Long restaurantId
    ) {
        System.out.println("유저네임?? : " + userDetails.getUsername());
        List<CategoryDTO> categoryListDTOList = categoryService.findCategoriesByRestaurant(restaurantId, userDetails);
        return new ResponseEntity<>(categoryListDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Create category", description = "Adds a new inventory category for the logged-in owner's restaurant.")
    @PostMapping("/save")
    public ResponseEntity<?> saveCategory(
            @RequestBody @Parameter(description = "Category data") CategoryDTO categoryDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        boolean success = categoryService.addCategory(categoryDTO, userDetails);
        if (success) {
            return ResponseEntity.ok().body("Category saved successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to save category");
        }
    }

    @Operation(summary = "Update category", description = "Updates an existing category's name or other properties.")
    @PutMapping("/update")
    public ResponseEntity<?> updateCategory(
            @RequestBody @Parameter(description = "Updated category data") CategoryDTO categoryDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        boolean success = categoryService.updateCategory(categoryDTO, userDetails);
        if (success) {
            return ResponseEntity.ok().body("Category updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update category");
        }
    }

    @Operation(summary = "Delete category", description = "Deletes an existing inventory category from the restaurant.")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(
            @RequestBody @Parameter(description = "Category to delete") CategoryDTO categoryDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        boolean success = categoryService.deleteCategory(categoryDTO, userDetails);
        if (success) {
            return ResponseEntity.ok().body("Category deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete category");
        }
    }
}