package com.web.mighigankoreancommunity.controller.announcement;

import com.web.mighigankoreancommunity.service.AnnouncementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/page/announcement")
@RequiredArgsConstructor
@Tag(name = "AnnouncementPageLoader", description = "Return All the pages related to announcement")
public class AnnouncementController {
    private final AnnouncementService announcementService;


    @GetMapping("/list")
    public String goAnnouncement(){
        return "announcement/announcement-list";
    }

    @GetMapping("/detail/{id}")
    public String getAnnouncement(@PathVariable Long id, @RequestParam Long restaurantId) {
        return "announcement/announcement-detail";
    }

    @GetMapping("/update/{id}")
    public String updateAnnouncement(@PathVariable Long id){
        return "announcement/announcement-update";
    }


    @GetMapping("/create")
    public String goCreateAnnouncement(){
        return "announcement/announcement-create";
    }
}
