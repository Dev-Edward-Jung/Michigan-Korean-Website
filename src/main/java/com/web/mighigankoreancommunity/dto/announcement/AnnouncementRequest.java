package com.web.mighigankoreancommunity.dto.announcement;


import com.web.mighigankoreancommunity.domain.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Request to create or update an announcement")
public class AnnouncementRequest {
    private String content; // 에디터 내용
    private String title;
    private ContentType type; // NOTICE, NORMAL
    private Long restaurantId; // 레스토랑 ID
    private Long ownerId; // 작성자가 사장님이면
    private Long restaurantEmployeeId; // 작성자가 직원이면
}
