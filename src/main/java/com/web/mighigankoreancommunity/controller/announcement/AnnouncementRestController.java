package com.web.mighigankoreancommunity.controller.announcement;

import com.web.mighigankoreancommunity.dto.AnnouncementRequest;
import com.web.mighigankoreancommunity.dto.AnnouncementResponse;
import com.web.mighigankoreancommunity.entity.Announcement;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/announcement")
@RequiredArgsConstructor
public class AnnouncementRestController {
    private final AnnouncementService announcementService;

    @GetMapping("/list")
    public void getAllAnnouncement(){}

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getAnnouncement(@PathVariable Long id, @RequestParam Long restaurantId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Object writer = null;
        if (userDetails.getOwner() != null) {
            writer = userDetails.getOwner();
        } else if (userDetails.getEmployee() != null) {
            writer = userDetails.getEmployee();
        } else {
            throw new RuntimeException("User is not logged in");
        }
        return new ResponseEntity<>(announcementService.getAnnouncement(id, restaurantId), HttpStatus.OK);
    }


    @PostMapping("/save")
    public ResponseEntity<Long> createAnnouncement(@RequestBody AnnouncementRequest request,
                                                   @RequestParam Long restaurantId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        System.out.println(request.getTitle());
        System.out.println(request.getContent());
        System.out.println(restaurantId);
        System.out.println(userDetails.getOwner().toString());
        Long id = announcementService.createAnnouncement(request, restaurantId, userDetails);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAnnouncement(@PathVariable Long id, @RequestBody AnnouncementRequest request,
                                                @RequestParam Long restaurantId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        AnnouncementResponse announcementResponse = announcementService.updateAnnouncement(id, request, restaurantId, userDetails);
        return new ResponseEntity<>(announcementResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id, @RequestParam Long restaurantId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        announcementService.deleteAnnouncement(id, restaurantId, userDetails);
        return ResponseEntity.ok("Delete Success");
    }

}
