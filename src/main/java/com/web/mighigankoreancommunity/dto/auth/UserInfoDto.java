package com.web.mighigankoreancommunity.dto.auth;


import com.web.mighigankoreancommunity.domain.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserInfoDto {
    public Long id;
    public MemberRole memberRole;
    public String email;
    public Long CurrentRestaurantId;

    public UserInfoDto(MemberRole memberRole, String email, Long CurrentRestaurantId) {
        this.memberRole = memberRole;
        this.email = email;
        this.CurrentRestaurantId = CurrentRestaurantId;
    }

}
