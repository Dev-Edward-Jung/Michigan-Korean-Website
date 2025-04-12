package com.web.mighigankoreancommunity.repository.inevntory;

import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findCategoriesByRestaurant(Restaurant restaurant);

    public Category findCategoryById(Long id);
}
