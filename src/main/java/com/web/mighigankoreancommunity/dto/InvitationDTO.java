package com.web.mighigankoreancommunity.dto;

import com.web.mighigankoreancommunity.domain.MemberRole;

public class InvitationDTO {
    private String inviteToken;
    public String email;
    public Long restaurantId;
    public Long ownerId;
    public String token;
    public MemberRole memberRole;

}
