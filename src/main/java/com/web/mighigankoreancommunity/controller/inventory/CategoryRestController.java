package com.web.mighigankoreancommunity.controller.inventory;


import com.web.mighigankoreancommunity.dto.CategoryDTO;
import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.CustomUserDetails;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.inevntory.CategoryRepository;
import com.web.mighigankoreancommunity.service.inventory.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/category")
@RestController
public class CategoryRestController {
    private final CategoryService categoryService;
    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> findCategories(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestParam Long restaurantId) {
        List<CategoryDTO> categoryListDTOList = categoryService.findCategoriesByRestaurant(restaurantId, userDetails.getOwner());
        return new ResponseEntity<>(categoryListDTOList, HttpStatus.OK);

    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDTO categoryDTO,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean categorySuccess =  categoryService.addCategory(categoryDTO, userDetails.getOwner());
        if (categorySuccess) {
            return ResponseEntity.ok().body("Category saved successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to save category");
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO,
                               @AuthenticationPrincipal CustomUserDetails userDetails){
        boolean categorySuccess = categoryService.updateCategory(categoryDTO, userDetails.getOwner());
        if (categorySuccess) {
            return ResponseEntity.ok().body("Category saved successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to save category");
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestBody CategoryDTO categoryDTO,
                               @AuthenticationPrincipal CustomUserDetails userDetails){
        boolean categorySuccess = categoryService.deleteCategory(categoryDTO, userDetails.getOwner());
        if (categorySuccess) {
            return ResponseEntity.ok().body("Category saved successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to save category");
        }
    }



}
