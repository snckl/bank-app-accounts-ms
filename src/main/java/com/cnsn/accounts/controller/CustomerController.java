package com.cnsn.accounts.controller;

import com.cnsn.accounts.dto.CustomerDetailsDto;
import com.cnsn.accounts.dto.ErrorResponseDto;
import com.cnsn.accounts.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "REST API for accounts of Bank",
        description = "Rest API for fetching customer details."
)
@RestController // Produces make rest api to support json as response.
@RequestMapping(path = "/api/v1",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final ICustomerService iCustomerService;

    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Operation(summary = "Fetch customer details details REST API",
            description = "Fetches customer details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Internal Server ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})",
                                                                   message = "Mobile number should be 10 digits")
                                                                   String mobileNumber,@RequestHeader("bank-correlation-id")
                                                                   String correlationId){

        logger.debug("bank-correlation-id found {}",correlationId);
        CustomerDetailsDto customerDetailsDto = iCustomerService.fetchCustomerDetails(mobileNumber,correlationId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDetailsDto);
    }

}
