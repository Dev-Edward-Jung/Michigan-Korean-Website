package com.web.mighigankoreancommunity.dto;


import com.web.mighigankoreancommunity.domain.ContentType;
import com.web.mighigankoreancommunity.entity.Announcement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnouncementResponse {
    private Long id;
    private String title;
    private String content;
    private ContentType type;
    private String writerName;
    private String restaurantName;
    private String createdAt;

    public static AnnouncementResponse from(Announcement announcement) {
        AnnouncementResponse res = new AnnouncementResponse();
        res.setId(announcement.getId());
        res.setContent(announcement.getContent());
        res.setType(announcement.getType());
        res.setCreatedAt(announcement.getCreatedAt().toString());
        res.setRestaurantName(announcement.getRestaurant().getName());

        if (announcement.getOwner() != null) {
            res.setWriterName(announcement.getOwner().getOwnerName());
        } else if (announcement.getRestaurantEmployee() != null) {
            res.setWriterName(announcement.getRestaurantEmployee().getEmployee().getName());
        } else {
            res.setWriterName("Not Found");
        }

        return res;
    }
}
