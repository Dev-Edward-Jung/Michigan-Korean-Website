package com.web.mighigankoreancommunity.repository.inevntory;

import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Optional<List<Category>> findCategoriesByRestaurant(Restaurant restaurant);

    public Optional<Category> findCategoryById(Long id);
}
