package com.web.mighigankoreancommunity.dto.payroll;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter

public class PayrollResponse {
    public Long id;
    public String name;
    public BigDecimal hourlyWage;

}
