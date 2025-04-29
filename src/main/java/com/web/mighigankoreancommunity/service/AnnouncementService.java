package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.domain.ContentType;
import com.web.mighigankoreancommunity.dto.AnnouncementRequest;
import com.web.mighigankoreancommunity.dto.AnnouncementResponse;
import com.web.mighigankoreancommunity.entity.*;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.repository.AnnouncementRepository;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.employee.RestaurantEmployeeRepository;
import com.web.mighigankoreancommunity.repository.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final RestaurantRepository restaurantRepository;
    private final OwnerRepository ownerRepository;
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;

    @Transactional
    public Long createAnnouncement(AnnouncementRequest request, Long restaurantId, CustomUserDetails userDetails) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        ContentType type = request.getType();
        System.out.println(restaurant.toString());

        Owner owner = null;
        RestaurantEmployee employee = null;

        if (userDetails.getOwner() != null) {
            owner = userDetails.getOwner();
        } else if (userDetails.getEmployee() != null) {
            System.out.println("Employee 들어옴?");
            Long employeeId = userDetails.getEmployee().getId();
            employee = restaurantEmployeeRepository.findRestaurantEmployeeByRestaurant_IdAndEmployee_Id(restaurantId, employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        } else {
            throw new IllegalArgumentException("Writer Not Found");
        }

        Announcement announcement = Announcement.create(
                request.getContent(),
                request.getTitle(),
                type,
                restaurant,
                owner,
                employee
        );

        return announcementRepository.save(announcement).getId();
    }

    @Transactional(readOnly = true)
    public AnnouncementResponse getAnnouncement(Long id, Long restaurantId) {
        Announcement announcement = announcementRepository.findAnnouncementByIdAndRestaurant_Id(id, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));
        return AnnouncementResponse.from(announcement);
    }

    @Transactional
    public AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest request, Long restaurantId, CustomUserDetails userDetails) {
        Announcement announcement = announcementRepository.findAnnouncementByIdAndRestaurant_Id(id, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

        // 🔥 작성자 정보 설정
        if (userDetails.getOwner() != null) {
            announcement.setOwner(userDetails.getOwner());
            announcement.setRestaurantEmployee(null); // 다른 필드 클리어
        } else if (userDetails.getRestaurantEmployee() != null) {
            announcement.setRestaurantEmployee(userDetails.getRestaurantEmployee());
            announcement.setOwner(null); // 다른 필드 클리어
        } else {
            throw new IllegalArgumentException("User is not authorized to update this announcement");
        }

        // 🔄 내용 수정
        announcement.update(
                request.getContent(),
                request.getType()
        );

        return AnnouncementResponse.from(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long id, Long restaurantId, UserDetails userDetails) {
        Announcement announcement = announcementRepository.findAnnouncementByIdAndRestaurant_Id(id, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

        announcementRepository.delete(announcement);
    }

    @Transactional(readOnly = true)
    public Page<AnnouncementResponse> getAllAnnouncements(Long restaurantId, int page, int size) {
        Sort sort = Sort.by(
                Sort.Order.desc("type"),        // NOTICE → NORMAL 순
                Sort.Order.desc("createdAt")    // 최신순
        );
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Announcement> announcements = announcementRepository.findByRestaurantId(restaurantId, pageable);
        return announcements.map(AnnouncementResponse::from);
    }
}
