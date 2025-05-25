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
    public CategoryDTO addCategory(CategoryDTO categoryDTO, CustomUserDetails loginUser) {
        // 1. 레스토랑 존재 확인
        Restaurant restaurant = restaurantRepository.findById(categoryDTO.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found."));

        // 2. 권한 확인
        if (!hasPermission(restaurant, loginUser)) {
            throw new RuntimeException("Unauthorized to add category");
        }

        // 3. DTO → Entity 변환
        Category category = Category.from(categoryDTO); // DTO에서 id, name만 설정됨
        category.setRestaurant(restaurant); // restaurant는 따로 직접 연결해야 함

        // 4. 저장
        Category saved = categoryRepository.save(category);

        // 5. Entity → DTO 변환 후 반환
        return CategoryDTO.from(saved);
    }

    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, CustomUserDetails loginUser) {
        // 1. 레스토랑 조회 및 권한 확인
        Restaurant restaurant = restaurantRepository.findById(categoryDTO.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found."));

        if (!hasPermission(restaurant, loginUser)) {
            throw new RuntimeException("Unauthorized to update category");
        }

        // 2. 기존 카테고리 조회
        Category existing = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));

        // 3. 내용 업데이트
        existing.setName(categoryDTO.getName());

        // 4. 저장
        Category saved = categoryRepository.save(existing);

        // 5. DTO로 변환하여 반환
        return CategoryDTO.from(saved);
    }

    @Transactional
    public void deleteCategory(CategoryDTO categoryDTO, CustomUserDetails loginUser) {
        Restaurant restaurant = restaurantRepository.findById(categoryDTO.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found."));

        if (!hasPermission(restaurant, loginUser)) {
            throw new RuntimeException("Unauthorized to delete category");
        }

        Category category = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));

        categoryRepository.delete(category);
    }
}
