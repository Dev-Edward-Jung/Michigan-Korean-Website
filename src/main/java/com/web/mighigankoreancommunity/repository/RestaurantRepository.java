package com.web.mighigankoreancommunity.repository;


import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

//    get ALl Restaurant(I do not use limit)
    List<Restaurant> findRestaurantsByOwnerId(Long id);

    List<Restaurant> findRestaurantsByOwner(Owner owner);


    Restaurant findRestaurantByIdAndOwner(Long id, Owner loginUser);


}
