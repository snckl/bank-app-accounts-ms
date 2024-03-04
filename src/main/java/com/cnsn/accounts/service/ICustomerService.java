package com.cnsn.accounts.service;

import com.cnsn.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {


    CustomerDetailsDto fetchCustomerDetails(String mobileNumber,String correlationId);
}
