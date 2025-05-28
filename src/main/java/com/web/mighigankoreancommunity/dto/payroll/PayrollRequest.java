package com.web.mighigankoreancommunity.dto.payroll;


import com.web.mighigankoreancommunity.entity.Payroll;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@ToString
public class PayrollRequest {
    public Long id;
    public String name;
    public BigDecimal hourlyWage;
    public BigDecimal totalWage;

    public PayrollRequest from(Payroll payroll) {
        return PayrollRequest.builder()
                .hourlyWage(payroll.getHourlyWage())
                .totalWage(payroll.getTotalWage())
                .build();
    }

}
