package com.cnsn.accounts.service.client;

import com.cnsn.accounts.dto.CardsDto;
import com.cnsn.accounts.dto.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "loans",fallback = LoansFallback.class) // connects to cards api in runtime which uses name in eureka
public interface LoansFeignClient {

    // method input param,return param,method access type should be same as in cards api
    @GetMapping(value = "/api/v1/fetch",consumes = "application/json")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam String mobileNumber,
                                                     @RequestHeader("bank-correlation-id") String correlationId);

}
