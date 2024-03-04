package com.cnsn.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Schema(
        name="Accounts",
        description =  "Account schema which holds the account information of the customer"
)
@Data
public class AccountsDto {
    @Schema(
            description =  "Account number of the customer",example = "1234567890"
    )
    @NotEmpty(message = "Account number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Account number must be 10 digits")
    private Long accountNumber;

    @Schema(
            description =  "Account type of the customer",example = "Savings"
    )
    @NotEmpty(message = "Account type cannot be empty")
    private String accountType;

    @Schema(
            description =  "Account address of the customer",example = "252St. NY/Mns"
    )
    @NotEmpty(message = "Branch address cannot be empty")
    private String branchAddress;
}
