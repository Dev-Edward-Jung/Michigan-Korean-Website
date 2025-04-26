package com.web.mighigankoreancommunity.controller.announcement;

import com.web.mighigankoreancommunity.dto.AnnouncementRequest;
import com.web.mighigankoreancommunity.dto.AnnouncementResponse;
import com.web.mighigankoreancommunity.entity.Announcement;
import com.web.mighigankoreancommunity.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAnnouncement(@PathVariable Long id){
        return new ResponseEntity<>(announcementService.getAnnouncement(id), HttpStatus.OK);
    }


    @PostMapping("/save")
    public ResponseEntity<Long> createAnnouncement(@RequestBody AnnouncementRequest request) {
        Long id = announcementService.createAnnouncement(request);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAnnouncement(@PathVariable Long id, @RequestBody AnnouncementRequest request) {
        AnnouncementResponse announcementResponse = announcementService.updateAnnouncement(id, request);
        return new ResponseEntity<>(announcementResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok("Delete Success");
    }

}
