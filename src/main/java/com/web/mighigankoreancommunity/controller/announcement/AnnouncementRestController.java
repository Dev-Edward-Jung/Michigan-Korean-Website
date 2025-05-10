package com.web.mighigankoreancommunity.controller.announcement;

import com.web.mighigankoreancommunity.dto.announcement.AnnouncementRequest;
import com.web.mighigankoreancommunity.dto.announcement.AnnouncementResponse;
import com.web.mighigankoreancommunity.dto.PageResponse;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/announcement")
@RequiredArgsConstructor
@Tag(name = "Announcement", description = "APIs for managing restaurant announcements")
public class AnnouncementRestController {

    private final AnnouncementService announcementService;

    @Operation(summary = "Get announcement list", description = "Retrieves a paginated list of announcements for a specific restaurant.")
    @GetMapping("/list")
    public ResponseEntity<PageResponse<AnnouncementResponse>> getAnnouncementList(
            @Parameter(description = "Restaurant ID") @RequestParam Long restaurantId,
            @Parameter(description = "Page number (starts from 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size
    ) {
        Page<AnnouncementResponse> announcementPage = announcementService.getAllAnnouncements(restaurantId, page, size);
        PageResponse<AnnouncementResponse> pageResponse = new PageResponse<>(
                announcementPage.getContent(),
                announcementPage.getNumber(),
                announcementPage.getSize(),
                announcementPage.getTotalElements(),
                announcementPage.getTotalPages(),
                announcementPage.isLast()
        );
        return ResponseEntity.ok(pageResponse);
    }

    @Operation(summary = "Get announcement detail", description = "Fetches detailed information about a specific announcement.")
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getAnnouncement(
            @Parameter(description = "Announcement ID") @PathVariable Long id,
            @Parameter(description = "Restaurant ID") @RequestParam Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Object writer = (userDetails.getOwner() != null) ? userDetails.getOwner() : userDetails.getEmployee();
        if (writer == null) throw new RuntimeException("User is not logged in");
        return new ResponseEntity<>(announcementService.getAnnouncement(id, restaurantId), HttpStatus.OK);
    }

    @Operation(summary = "Create announcement", description = "Creates a new announcement for the given restaurant.")
    @PostMapping("/save")
    public ResponseEntity<Long> createAnnouncement(
            @RequestBody AnnouncementRequest request,
            @RequestParam Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long id = announcementService.createAnnouncement(request, restaurantId, userDetails);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "Update announcement", description = "Updates an existing announcement.")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAnnouncement(
            @PathVariable Long id,
            @RequestBody AnnouncementRequest request,
            @RequestParam Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AnnouncementResponse updated = announcementService.updateAnnouncement(id, request, restaurantId, userDetails);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(summary = "Delete announcement", description = "Deletes a specific announcement.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnnouncement(
            @PathVariable Long id,
            @RequestParam Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        announcementService.deleteAnnouncement(id, restaurantId, userDetails);
        return ResponseEntity.ok("Delete Success");
    }
}
