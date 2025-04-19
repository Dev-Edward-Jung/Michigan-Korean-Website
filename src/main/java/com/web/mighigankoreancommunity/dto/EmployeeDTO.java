package com.web.mighigankoreancommunity.dto;


import com.web.mighigankoreancommunity.domain.MemberRole;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeDTO {
    Long id;
    String name;
    String email;
    MemberRole memberRole;
    Long restaurantId;
    List<ScheduleDTO> schedules;
    LocalDate shiftStartDate;
    LocalDate shiftEndDate;

    public EmployeeDTO (Long id, String name, String email, MemberRole memberRole, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.memberRole = memberRole;
        this.restaurantId = restaurantId;
    }
}
