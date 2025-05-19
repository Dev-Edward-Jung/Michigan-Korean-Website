package com.web.mighigankoreancommunity.dto.schedule;


import com.web.mighigankoreancommunity.dto.employee.EmployeeDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Setter
@Getter
public class RestaurantScheduleRequest {
    List<EmployeeDTO> employees;
    LocalDate startDate;
    LocalDate endDate;
}
