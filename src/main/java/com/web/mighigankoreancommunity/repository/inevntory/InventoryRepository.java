package com.web.mighigankoreancommunity.repository.inevntory;


import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

//    get all inventories
    @Query("SELECT i FROM Inventory i where i.restaurant.id = :restaurantId")
    Optional<List<Inventory>> findByRestaurantsId(Long restaurantId);

    boolean existsByNameAndRestaurant(String name, Restaurant restaurant);






}
