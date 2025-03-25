package com.web.mighigankoreancommunity.dto;


import com.web.mighigankoreancommunity.entity.Restaurant;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Data
public class MemberDTO {
    private long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private List<Restaurant> restaurants;
}
