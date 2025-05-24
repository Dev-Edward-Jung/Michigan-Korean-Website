package com.web.mighigankoreancommunity.controller.payroll;


import com.web.mighigankoreancommunity.dto.payroll.PayrollRequest;
import com.web.mighigankoreancommunity.dto.payroll.PayrollResponse;
import com.web.mighigankoreancommunity.entity.Payroll;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payroll")
public class PayrollRestController {
    private final PayrollService payrollService;

    @GetMapping("/list")
    public ResponseEntity<?> getPayrolls(@RequestParam Long restaurantId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if(customUserDetails.isOwner()) {
            List<PayrollResponse> payrollResponses = payrollService.getAllPayrolls(restaurantId, customUserDetails.getOwner());
            return new ResponseEntity<>(payrollResponses, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/save")
    public void savePayroll(PayrollRequest request, @RequestParam("restaurantId") Long restaurantId) {
        System.out.println("Restaurant Id: " + restaurantId);
        System.out.println("Payroll request: " + request.getName());
    }

    @PutMapping("/update")
    public void updatePayroll(Payroll p) {}


    @DeleteMapping("/delete")
    public void deletePayroll(Payroll p) {}


}
