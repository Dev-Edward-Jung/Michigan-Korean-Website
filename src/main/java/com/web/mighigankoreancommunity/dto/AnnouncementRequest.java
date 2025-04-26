package com.web.mighigankoreancommunity.dto;


import com.web.mighigankoreancommunity.domain.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnouncementRequest {
    private String content; // 에디터 내용
    private ContentType type; // NOTICE, NORMAL
    private Long restaurantId; // 레스토랑 ID
    private Long ownerId; // 작성자가 사장님이면
    private Long restaurantEmployeeId; // 작성자가 직원이면
}
