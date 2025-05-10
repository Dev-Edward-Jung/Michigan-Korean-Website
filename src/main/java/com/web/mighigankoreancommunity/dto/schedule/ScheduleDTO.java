package com.web.mighigankoreancommunity.dto.schedule;

import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.domain.Shift;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleDTO {
    public Shift shift;
    public Long restaurantId;
    public Long employeeId;
    public String name;
    public LocalDate shiftStartDate;
    public LocalDate shiftEndDate;
    public MemberRole memberRole;

}
