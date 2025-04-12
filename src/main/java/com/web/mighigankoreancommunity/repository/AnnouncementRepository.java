package com.web.mighigankoreancommunity.repository;


import com.web.mighigankoreancommunity.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}
