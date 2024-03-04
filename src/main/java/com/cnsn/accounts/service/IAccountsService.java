package com.cnsn.accounts.service;

import com.cnsn.accounts.dto.CustomerDto;

public interface IAccountsService   {
    void createAccount(CustomerDto customerDto);
    CustomerDto fetchAccount(String mobileNumber);
    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);
}
