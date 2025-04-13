package com.web.mighigankoreancommunity.dto;

import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.domain.Shift;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

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
