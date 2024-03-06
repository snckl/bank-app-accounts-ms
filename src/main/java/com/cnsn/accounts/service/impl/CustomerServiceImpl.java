package com.cnsn.accounts.service.impl;

import com.cnsn.accounts.dto.AccountsDto;
import com.cnsn.accounts.dto.CardsDto;
import com.cnsn.accounts.dto.CustomerDetailsDto;
import com.cnsn.accounts.dto.LoansDto;
import com.cnsn.accounts.entity.Accounts;
import com.cnsn.accounts.entity.Customer;
import com.cnsn.accounts.exception.ResourceNotFoundException;
import com.cnsn.accounts.service.client.CardsFeignClient;
import com.cnsn.accounts.service.client.LoansFeignClient;
import com.cnsn.accounts.mapper.AccountsMapper;
import com.cnsn.accounts.mapper.CustomerMapper;
import com.cnsn.accounts.repository.AccountsRepository;
import com.cnsn.accounts.repository.CustomerRepository;
import com.cnsn.accounts.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    @Qualifier("com.cnsn.accounts.service.client.CardsFeignClient")
    private final CardsFeignClient cardsFeignClient;
    @Qualifier("com.cnsn.accounts.service.client.LoansFeignClient")
    private final LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber,String correlationId) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer","mobile number",mobileNumber)
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account","customer ID",customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer,new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts,new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber,correlationId);
        if(loansDtoResponseEntity != null) customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber,correlationId);
        if(cardsDtoResponseEntity != null) customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
