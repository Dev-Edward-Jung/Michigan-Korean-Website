package com.web.mighigankoreancommunity.service.inventory;


import com.web.mighigankoreancommunity.dto.CategoryDTO;
import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.inevntory.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public List<CategoryDTO> findCategoriesByRestaurant(Long restaurantId, Owner owner) {
            Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(restaurantId, owner);
            List<Category> categoryList= categoryRepository.findCategoriesByRestaurant(restaurant);
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

    public boolean addCategory( CategoryDTO categoryDTO, Owner owner) {
        Long restaurantId = categoryDTO.getRestaurantId();
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(restaurantId, owner);
        if (restaurant == null) {
            return false;
        }
        Category category = Category.builder()
                        .name(categoryDTO.getName())
                        .restaurant(restaurant)
                        .build();

        categoryRepository.save(category);
        return true;
    }


    public boolean updateCategory( CategoryDTO categoryDTO, Owner owner) {
        Long restaurantId = categoryDTO.getRestaurantId();
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(restaurantId, owner);
        if (restaurant == null) {
            return false;
        }
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .restaurant(restaurant)
                .id(categoryDTO.getId())
                .build();

        categoryRepository.save(category);
        return true;
    }

    public boolean deleteCategory(CategoryDTO categoryDTO, Owner owner) {
        Long restaurantId = categoryDTO.getRestaurantId();
        Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(restaurantId, owner);
        if (restaurant == null) {
            return false;
        }
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .restaurant(restaurant)
                .id(categoryDTO.getId())
                .build();

        categoryRepository.delete(category);

        return true;
    }
}
