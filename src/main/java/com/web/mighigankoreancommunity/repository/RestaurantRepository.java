package com.web.mighigankoreancommunity.repository;


import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

//    get ALl Restaurant(I do not use limit)
    Optional<List<Restaurant>> findRestaurantsByOwnerId(Long id);

    Optional<List<Restaurant>> findRestaurantsByOwner(Owner owner);


    Optional<Restaurant> findRestaurantByIdAndOwner(Long id, Owner loginUser);


}
