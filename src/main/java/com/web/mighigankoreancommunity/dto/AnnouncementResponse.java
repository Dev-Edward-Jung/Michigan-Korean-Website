package com.web.mighigankoreancommunity.dto;


import com.web.mighigankoreancommunity.domain.ContentType;
import com.web.mighigankoreancommunity.entity.Announcement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Response object containing announcement details")
public class AnnouncementResponse {
    private Long id;
    private String title;
    private String content;
    private ContentType type;
    private Long writerId;
    private String writerName;
    private String restaurantName;
    private String createdAt;

    public static AnnouncementResponse from(Announcement announcement) {
        AnnouncementResponse res = new AnnouncementResponse();
        res.setId(announcement.getId());
        res.setTitle(announcement.getTitle());
        res.setContent(announcement.getContent());
        res.setType(announcement.getType());
        res.setRestaurantName(announcement.getRestaurant().getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        res.setCreatedAt(announcement.getCreatedAt().format(formatter));

        if (announcement.getOwner() != null) {
            res.setWriterId(announcement.getOwner().getId()); //  owner id 설정
            res.setWriterName(announcement.getOwner().getOwnerName());
        } else if (announcement.getRestaurantEmployee() != null) {
            res.setWriterId(announcement.getRestaurantEmployee().getId()); //  employee id 설정
            res.setWriterName(announcement.getRestaurantEmployee().getEmployee().getName());
        } else {
            res.setWriterId(null); // 없는 경우 null 처리
            res.setWriterName("Not Found");
        }

        return res;
    }
}
