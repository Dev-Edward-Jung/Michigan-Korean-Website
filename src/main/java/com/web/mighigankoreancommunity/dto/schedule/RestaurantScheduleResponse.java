package com.web.mighigankoreancommunity.dto.schedule;


import com.web.mighigankoreancommunity.dto.employee.EmployeeDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class RestaurantScheduleResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<String, List<EmployeeDTO>> employees;
}
