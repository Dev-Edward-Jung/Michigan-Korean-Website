package com.web.mighigankoreancommunity.repository.employee;

import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantEmployeeRepository  extends JpaRepository<RestaurantEmployee, Long> {
    public List<RestaurantEmployee> findRestaurantEmployeesByRestaurant_Id(Long restaurantId);

    public RestaurantEmployee findRestaurantEmployeeByRestaurant_IdAndEmployee_Id(Long restaurantId, Long employeeId);

    public boolean existsByRestaurant_IdAndMemberRole(Long restaurantId, MemberRole memberRole);
}
