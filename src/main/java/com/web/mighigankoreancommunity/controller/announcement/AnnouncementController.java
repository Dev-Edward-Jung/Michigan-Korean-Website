package com.web.mighigankoreancommunity.controller.announcement;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/page/announcement")
@RequiredArgsConstructor
public class AnnouncementController {


    @GetMapping("/list")
    public String goAnnouncement(){
        return "owner-pages/announcement-list";
    }
}
