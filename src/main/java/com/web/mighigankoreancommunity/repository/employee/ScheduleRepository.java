package com.web.mighigankoreancommunity.repository.employee;

import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import com.web.mighigankoreancommunity.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    public Optional<List<Schedule>> findSchedulesByRestaurantEmployee_Restaurant_Id(Long restaurantId);

    public Optional<List<Schedule>> findSchedulesByRestaurantEmployee(RestaurantEmployee restaurantEmployee);

    public Optional<Schedule> findScheduleByRestaurantEmployee(RestaurantEmployee restaurantEmployee);
}
