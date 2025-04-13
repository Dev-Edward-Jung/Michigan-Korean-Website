package com.web.mighigankoreancommunity.dto;


import com.web.mighigankoreancommunity.domain.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    Long id;
    String name;
    String email;
    String phone;
    MemberRole memberRole;
    Long restaurantId;
    List<ScheduleDTO> schedules;
}
