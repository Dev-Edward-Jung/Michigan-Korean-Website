package com.web.mighigankoreancommunity.dto;


import com.web.mighigankoreancommunity.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Data
public class MemberDTO {
    private long id;
    private String name;
    private String email;
}
