package com.web.mighigankoreancommunity.controller.inventory;


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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/category")
@RestController
public class CategoryRestController {
    private final CategoryService categoryService;
    @GetMapping("/list")
    public ResponseEntity<List<Category>> findCategories(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestParam Long restaurantId) {
        List<Category> categoryList = categoryService.findCategoriesByRestaurant(restaurantId, userDetails.getOwner());
        return new ResponseEntity<>(categoryList, HttpStatus.OK);

    }

}
