package com.web.mighigankoreancommunity.repository.employee;

import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantEmployeeRepository  extends JpaRepository<RestaurantEmployee, Long> {
}
