package com.web.mighigankoreancommunity.repository;

import com.web.mighigankoreancommunity.entity.Payroll;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    public Optional<Payroll>findPayrollByRestaurantEmployee (RestaurantEmployee restaurantEmployee);

}
