package com.cnsn.accounts.controller;

import com.cnsn.accounts.constants.AccountsConstants;
import com.cnsn.accounts.dto.AccountsContactInfoDto;
import com.cnsn.accounts.dto.CustomerDto;
import com.cnsn.accounts.dto.ErrorResponseDto;
import com.cnsn.accounts.dto.ResponseDto;
import com.cnsn.accounts.service.IAccountsService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/*
  - REMINDER FOR MYSELF -
  @RequestBody - json input
  @RequestParam - url param x?param
  @PathVariable - url variable x/variable
*/

@Tag(
        name = "CRUD API for accounts of Bank",
        description = "Create update fetch delete for account details"
)
@RestController // Produces make rest api to support json as response.
@RequestMapping(path = "/api/v1",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
public class AccountsController {

      private final IAccountsService iAccountsService;

      @Value("${build.version}")
      private String buildVersion;

      @Autowired
      private Environment environment;

      private final AccountsContactInfoDto accountsContactInfoDto;

      @Operation(summary = "Create account REST API",
                  description = "Creates new customer and account with input of customerDto"
      )
      @ApiResponse(
              responseCode = "201",
              description = "HTTP Status CREATED"
      )
      @PostMapping("/create")
      public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto){
        iAccountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED) // This one goes to header.
                .body(new ResponseDto(AccountsConstants.STATUS_201,AccountsConstants.MESSAGE_201));
      }


      @Operation(summary = "Fetch details REST API",
              description = "Fetches account details"
      )
      @ApiResponse(
              responseCode = "200",
              description = "HTTP Status OK"
      )
      @Retry(name = "fetchAccountDetails",fallbackMethod = "fetchAccountDetailsFallBack")
      @GetMapping("/fetch")
      public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam String mobileNumber){
            CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
            return ResponseEntity.
                    status(HttpStatus.OK)
                    .body(customerDto);
      }
      public ResponseEntity<CustomerDto> fetchAccountDetailsFallBack(@RequestParam String mobileNumber,Throwable throwable){
            return ResponseEntity.
                    status(HttpStatus.OK)
                    .body(null);
      }
      @Operation(summary = "Update account REST API",
              description = "Updates customer and account with input of customerDto"
      )
      @ApiResponses({
              @ApiResponse(
                      responseCode = "200",
                      description = "HTTP Status OK"),
              @ApiResponse(
                      responseCode = "417",
                      description = "EXPECTATION FAILED",
                      content = @Content(
                              schema = @Schema(implementation = ErrorResponseDto.class)
                      )
              )
      })
      @PutMapping("/update")
      public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto){
            boolean isUpdated = iAccountsService.updateAccount(customerDto);
            if(isUpdated){
                  return ResponseEntity
                          .status(HttpStatus .OK)
                          .body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
            } else {
                  return ResponseEntity
                          .status(HttpStatus.EXPECTATION_FAILED)
                          .body(new ResponseDto(AccountsConstants.STATUS_417,AccountsConstants.MESSAGE_417_UPDATE));
            }
      }

      @Operation(summary = "Delete account REST API",
              description = "Delete customer and account with input of mobileNumber"
      )

      @ApiResponses({
              @ApiResponse(
                      responseCode = "200",
                      description = "HTTP Status OK"),
              @ApiResponse(
                      responseCode = "417",
                      description = "EXPECTATION FAILED"
              )
      })
      @DeleteMapping("/delete")
      public ResponseEntity<ResponseDto> deleteAccount(@RequestParam String mobileNumber){
            boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
            if(isDeleted){
                  return ResponseEntity
                          .status(HttpStatus.OK)
                          .body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
            } else {
                  return ResponseEntity
                          .status(HttpStatus.EXPECTATION_FAILED)
                          .body(new ResponseDto(AccountsConstants.STATUS_417,AccountsConstants.MESSAGE_417_DELETE));
            }
      }



      @Operation(summary = "Build info REST API",
              description = "Return build info"
      )
      @ApiResponses({
              @ApiResponse(
                      responseCode = "200",
                      description = "HTTP Status OK"),
              @ApiResponse(
                      responseCode = "500",
                      description = "Internal server error",
                      content=@Content(
                              schema = @Schema(implementation = ErrorResponseDto.class)
                      )
              )
      })
      @GetMapping("/build-info")
      public ResponseEntity<String> getBuildInfo(){
            return ResponseEntity.
                    status(HttpStatus.OK)
                    .body(buildVersion);
      }

      @Operation(summary = "Java version info REST API",
              description = "Return java version info"
      )
      @ApiResponses({
              @ApiResponse(
                      responseCode = "200",
                      description = "HTTP Status OK"),
              @ApiResponse(
                      responseCode = "500",
                      description = "Internal server error",
                      content=@Content(
                              schema = @Schema(implementation = ErrorResponseDto.class)
                      )
              )
      })
      @GetMapping("/contact")
      public ResponseEntity<AccountsContactInfoDto> getContactInfo(){
            return ResponseEntity.
                    status(HttpStatus.OK)
                    .body(accountsContactInfoDto);
      }

      @Operation(summary = "Contact info REST API",
              description = "Return contact information"
      )
      @ApiResponses({
              @ApiResponse(
                      responseCode = "200",
                      description = "HTTP Status OK"),
              @ApiResponse(
                      responseCode = "500",
                      description = "Internal server error",
                      content=@Content(
                              schema = @Schema(implementation = ErrorResponseDto.class)
                      )
              )
      })
      @RateLimiter(name = "getJavaVersion",fallbackMethod = "getJavaVersionFallBack")
      @GetMapping("/java-version")
      public ResponseEntity<String> getJavaVersion(){
            return ResponseEntity.
                    status(HttpStatus.OK)
                    .body(environment.getProperty("MAVEN_HOME"));
      }

      public ResponseEntity<String> getJavaVersionFallBack(Throwable throwable){
            return ResponseEntity.
                    status(HttpStatus.OK)
                    .body("Java 17");
      }
}
