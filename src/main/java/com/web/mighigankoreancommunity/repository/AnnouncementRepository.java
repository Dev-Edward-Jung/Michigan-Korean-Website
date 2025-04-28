package com.web.mighigankoreancommunity.repository;


import com.web.mighigankoreancommunity.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    public Optional<Announcement> findAnnouncementByIdAndRestaurant_Id(Long id, Long restaurantId);

    Page<Announcement> findByRestaurantId(Long restaurantId, Pageable pageable);
}
