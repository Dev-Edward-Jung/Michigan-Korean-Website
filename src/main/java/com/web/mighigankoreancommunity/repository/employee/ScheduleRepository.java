package com.web.mighigankoreancommunity.repository.employee;

import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import com.web.mighigankoreancommunity.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    public List<Schedule> findSchedulesByRestaurantEmployee_Restaurant_Id(Long restaurantId);

    public List<Schedule> findSchedulesByRestaurantEmployee(RestaurantEmployee restaurantEmployee);

    public Schedule findScheduleByRestaurantEmployee(RestaurantEmployee restaurantEmployee);
}
