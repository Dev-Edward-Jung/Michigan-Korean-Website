package com.web.mighigankoreancommunity.repository.employee;

import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantEmployeeRepository  extends JpaRepository<RestaurantEmployee, Long> {
    public Optional<List<RestaurantEmployee>> findRestaurantEmployeesByRestaurant_IdAndApprovedTrue(Long restaurantId);

    public Optional<RestaurantEmployee> findRestaurantEmployeeByRestaurant_IdAndEmployee_Id(Long restaurantId, Long employeeId);

    public boolean existsByRestaurant_IdAndMemberRole(Long restaurantId, MemberRole memberRole);

    public boolean existsByEmployee_EmailAndRestaurant_Id(String email, Long employeeId);

//    we don't need to use Optional for findAll bc it will return empty list if it is empty
    public List<RestaurantEmployee> findAllByEmployee_Email(String email);

    Optional<RestaurantEmployee> findByEmployeeIdAndRestaurantId(Long employeeId, Long restaurantId);
}
