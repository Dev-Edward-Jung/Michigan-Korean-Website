package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.domain.ContentType;
import com.web.mighigankoreancommunity.dto.AnnouncementRequest;
import com.web.mighigankoreancommunity.dto.AnnouncementResponse;
import com.web.mighigankoreancommunity.entity.Announcement;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
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
            System.out.println("owner = " + owner.toString());
        } else if (userDetails.getEmployee() != null) {
            Long restaurantEmployeeId = request.getRestaurantEmployeeId();
            employee = restaurantEmployeeRepository.findByIdAndRestaurantId(restaurantEmployeeId, restaurantId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found or does not belong to this restaurant"));
            System.out.println("employee = " + employee.toString());
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
    public AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest request, Long restaurantId, UserDetails userDetails) {
        Announcement announcement = announcementRepository.findAnnouncementByIdAndRestaurant_Id(id, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

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

    @Transactional
    public Page<AnnouncementResponse> getAllAnnouncements(Long restaurantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Announcement> announcements = announcementRepository.findByRestaurantId(restaurantId, pageable);

        return announcements.map(AnnouncementResponse::from); // Entity → DTO 변환
    }
}
