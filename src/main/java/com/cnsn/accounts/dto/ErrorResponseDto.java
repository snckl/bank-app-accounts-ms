package com.cnsn.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(
        name = "Error Response",
        description = "Schema which hold error response"
)
@Data@AllArgsConstructor
public class ErrorResponseDto {
    @Schema(
            description = "API path invoked by the user",example = "/api/v1/fetchCustomer"
    )
    private String apiPath;
    @Schema(
            description = "Error code"
    )
    private HttpStatus errorCode;
    @Schema(
            description = "Error message for response"
    )
    private String errorMsg;
    @Schema(
            description = "DateTime / when error happened"
    )
    private LocalDateTime errorTime;

}
