package com.web.mighigankoreancommunity.dto.employee;


import com.web.mighigankoreancommunity.domain.MemberRole;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantEmployeeResponse {
    private Long id;
    private String name;
    private String email;
    private MemberRole memberRole;
    private Long restaurantId;
    private String lastMessage;
}
