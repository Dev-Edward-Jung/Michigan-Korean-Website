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
        System.out.println("category List : " + categoryListDTOList.toString());
        return new ResponseEntity<>(categoryListDTOList, HttpStatus.OK);

    }

    @PostMapping("/save")
    public void saveCategory(@RequestBody CategoryDTO categoryDTO,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        System.out.println("Category DTO : " + categoryDTO.toString());
        boolean categorySuccess =  categoryService.addCategory(categoryDTO, userDetails.getOwner());
        System.out.println("category save success : " + categorySuccess);
        System.out.println(categoryDTO.toString());

    }

    @PutMapping("/update")
    public void updateCategory(@RequestBody CategoryDTO categoryDTO,
                               @AuthenticationPrincipal CustomUserDetails userDetails){
        categoryService.updateCategory(categoryDTO, userDetails.getOwner());
        System.out.println(categoryDTO.toString());
    }

    @DeleteMapping("/delete")
    public void deleteCategory(@RequestBody CategoryDTO categoryDTO,
                               @AuthenticationPrincipal CustomUserDetails userDetails){
        categoryService.deleteCategory(categoryDTO, userDetails.getOwner());
    }



}
