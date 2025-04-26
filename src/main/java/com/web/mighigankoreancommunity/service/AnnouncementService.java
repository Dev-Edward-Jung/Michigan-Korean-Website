package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.domain.ContentType;
import com.web.mighigankoreancommunity.dto.AnnouncementRequest;
import com.web.mighigankoreancommunity.dto.AnnouncementResponse;
import com.web.mighigankoreancommunity.entity.Announcement;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import com.web.mighigankoreancommunity.repository.AnnouncementRepository;
import com.web.mighigankoreancommunity.repository.RestaurantRepository;
import com.web.mighigankoreancommunity.repository.employee.RestaurantEmployeeRepository;
import com.web.mighigankoreancommunity.repository.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
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
    public Long createAnnouncement(AnnouncementRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        ContentType type = request.getType();

        Owner owner = null;
        RestaurantEmployee employee = null;

        if (request.getOwnerId() != null) {
            owner = ownerRepository.findById(request.getOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        } else if (request.getRestaurantEmployeeId() != null) {
            employee = restaurantEmployeeRepository.findById(request.getRestaurantEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        } else {
            throw new IllegalArgumentException("Writer Not Found");
        }

        Announcement announcement = Announcement.create(
                request.getContent(),
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
    public AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest request, Long restaurantId) {
        Announcement announcement = announcementRepository.findAnnouncementByIdAndRestaurant_Id(id, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

        announcement.update(
                request.getContent(),
                request.getType()
        );

        return AnnouncementResponse.from(announcement);


    }

    @Transactional
    public void deleteAnnouncement(Long id, Long restaurantId) {
        Announcement announcement = announcementRepository.findAnnouncementByIdAndRestaurant_Id(id, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

        announcementRepository.delete(announcement);
    }
}
