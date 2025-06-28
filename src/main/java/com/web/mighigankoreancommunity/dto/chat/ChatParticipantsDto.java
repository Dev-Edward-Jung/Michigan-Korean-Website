package com.web.mighigankoreancommunity.dto.chat;

import com.web.mighigankoreancommunity.domain.MemberRole;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChatParticipantsDto {
    private Long userId;
    private String name;
    private String Email;
    private String profileImageUrl; // (선택) 프로필 이미지
    private MemberRole role;
}
