package com.web.mighigankoreancommunity.service.inventory;


import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.inevntory.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public List<Category> findCategoriesByRestaurant(Long restaurantId, Owner owner) {
            Restaurant restaurant = restaurantRepository.findRestaurantByIdAndOwner(restaurantId, owner);
        return categoryRepository.findCategoriesByRestaurant(restaurant);
    }
}
