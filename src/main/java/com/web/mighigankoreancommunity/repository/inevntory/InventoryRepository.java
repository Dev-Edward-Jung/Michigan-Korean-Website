package com.web.mighigankoreancommunity.repository.inevntory;


import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

//    get all inventories
    @Query("SELECT i FROM Inventory i WHERE i.restaurant.id = :restaurantId ORDER BY i.needNow DESC, i.name ASC")
    Page<Inventory> findByRestaurantIdPaged(@Param("restaurantId") Long restaurantId, Pageable pageable);

    boolean existsByNameAndRestaurant(String name, Restaurant restaurant);






}
