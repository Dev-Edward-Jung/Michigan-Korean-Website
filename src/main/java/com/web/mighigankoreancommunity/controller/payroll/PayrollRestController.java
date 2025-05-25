package com.web.mighigankoreancommunity.controller.payroll;


import com.web.mighigankoreancommunity.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<PayrollResponse>>> getPayrolls(@RequestParam Long restaurantId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if(customUserDetails.isOwner()) {
            List<PayrollResponse> payrollResponses = payrollService.getAllPayrolls(restaurantId, customUserDetails.getOwner());
            return ResponseEntity.ok(
                    ApiResponse.success(payrollResponses, "Update Successfully"));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<PayrollResponse>> updatePayroll(@RequestParam Long restaurantId, @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody PayrollRequest payrollRequest) {
//        PayrollResponse payrollResponse = payrollService.
    return null;
    }



    @DeleteMapping("/delete")
    public void deletePayroll(Payroll p) {}


}
