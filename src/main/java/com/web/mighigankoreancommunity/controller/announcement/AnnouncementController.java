package com.web.mighigankoreancommunity.controller.announcement;

import com.web.mighigankoreancommunity.dto.AnnouncementResponse;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.AnnouncementService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/page/announcement")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;


    @GetMapping("/list")
    public String goAnnouncement(){
        return "announcement/announcement-list";
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<AnnouncementResponse> getAnnouncement(@PathVariable Long id, @RequestParam Long restaurantId,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        AnnouncementResponse response = announcementService.getAnnouncement(id, restaurantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/create")
    public String goCreateAnnouncement(){
        return "announcement/announcement-create";
    }
}
