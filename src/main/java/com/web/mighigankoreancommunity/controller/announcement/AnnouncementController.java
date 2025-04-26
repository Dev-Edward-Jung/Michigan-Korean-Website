package com.web.mighigankoreancommunity.controller.announcement;

import com.web.mighigankoreancommunity.dto.AnnouncementResponse;
import com.web.mighigankoreancommunity.service.AnnouncementService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AnnouncementResponse> getAnnouncement(@PathVariable Long id) {
        AnnouncementResponse response = announcementService.getAnnouncement(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/create")
    public String goCreateAnnouncement(){
        return "announcement/announcement-create";
    }
}
