package com.endava.hotelApp.services;

import com.endava.hotelApp.domain.Customer;
import com.endava.hotelApp.dto.CustomerDTO;
import com.endava.hotelApp.exception.CustomerExistException;
import com.endava.hotelApp.exception.CustomerNotExistException;

import java.util.List;

public interface ICustomerService {
    void addCustomer(CustomerDTO customerDTO) throws CustomerExistException;
    void deleteCustomer(Integer customerId) throws CustomerNotExistException;
    List<Customer> customersSortedAlphabetically(String sort);
}
