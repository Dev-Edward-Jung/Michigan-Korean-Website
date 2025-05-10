package com.web.mighigankoreancommunity.service.inventory;


import com.web.mighigankoreancommunity.dto.category.CategoryDTO;
import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.error.RestaurantNotFoundException;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.inevntory.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    private boolean hasPermission(Restaurant restaurant, CustomUserDetails loginUser) {
        if (loginUser.isOwner()) {
            return restaurant.getOwner().getId().equals(loginUser.getOwner().getId());
        } else if (loginUser.isEmployee()) {
            return loginUser.getRestaurantEmployee() != null &&
                    loginUser.getRestaurantEmployee().getRestaurant().getId().equals(restaurant.getId());
        }
        return false;
    }

    public List<CategoryDTO> findCategoriesByRestaurant(Long restaurantId, CustomUserDetails loginUser) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found."));

        if (!(loginUser.isOwner() || loginUser.isEmployee())) {
            throw new RuntimeException("Unauthorized to view categories");
        }

        List<Category> categoryList = categoryRepository.findCategoriesByRestaurant(restaurant)
                .orElseThrow(() -> new RuntimeException("Category not found."));

        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        categoryList.forEach(category -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setRestaurantId(category.getRestaurant().getId());
            categoryDTOList.add(dto);
        });

        return categoryDTOList;
    }

    @Transactional
    public boolean addCategory(CategoryDTO categoryDTO, CustomUserDetails loginUser) {
        Restaurant restaurant = restaurantRepository.findById(categoryDTO.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found."));

        if (!hasPermission(restaurant, loginUser)) {
            throw new RuntimeException("Unauthorized to add category");
        }

        Category category = Category.builder()
                .name(categoryDTO.getName())
                .restaurant(restaurant)
                .build();

        categoryRepository.save(category);
        return true;
    }

    @Transactional
    public boolean updateCategory(CategoryDTO categoryDTO, CustomUserDetails loginUser) {
        Restaurant restaurant = restaurantRepository.findById(categoryDTO.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found."));

        if (!hasPermission(restaurant, loginUser)) {
            throw new RuntimeException("Unauthorized to update category");
        }

        Category category = Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .restaurant(restaurant)
                .build();

        categoryRepository.save(category);
        return true;
    }

    @Transactional
    public boolean deleteCategory(CategoryDTO categoryDTO, CustomUserDetails loginUser) {
        Restaurant restaurant = restaurantRepository.findById(categoryDTO.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found."));

        if (!hasPermission(restaurant, loginUser)) {
            throw new RuntimeException("Unauthorized to delete category");
        }

        Category category = Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .restaurant(restaurant)
                .build();

        categoryRepository.delete(category);
        return true;
    }
}
