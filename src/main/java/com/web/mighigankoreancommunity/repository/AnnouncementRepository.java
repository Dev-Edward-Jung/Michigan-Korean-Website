package com.web.mighigankoreancommunity.repository;


import com.web.mighigankoreancommunity.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    public Optional<Announcement> findAnnouncementByIdAndRestaurant_Id(Long id, Long restaurantId);
}
