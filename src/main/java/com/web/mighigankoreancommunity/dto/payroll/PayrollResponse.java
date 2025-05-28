package com.web.mighigankoreancommunity.dto.payroll;


import com.web.mighigankoreancommunity.entity.Payroll;
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
    public BigDecimal totalWage;

    public static PayrollResponse of(Payroll payroll) {
        return PayrollResponse.builder()
                .id(payroll.getId())
                .hourlyWage(payroll.getHourlyWage())
                .totalWage(payroll.getTotalWage())
                .build();
    }
}
