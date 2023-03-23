package com.endava.hotelApp.services;

import com.endava.hotelApp.domain.Customer;
import com.endava.hotelApp.dto.CustomerDTO;
import com.endava.hotelApp.exception.CustomerExistException;
import com.endava.hotelApp.exception.CustomerNotExistException;
import com.endava.hotelApp.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;


    public void addCustomer(CustomerDTO customerDTO) throws CustomerExistException {
        boolean exists = customerRepository.existsById(customerDTO.getCi());
        if(exists){
            throw new CustomerExistException("Cliente con ci: " + customerDTO.getCi() + " ya existe.");
        }
        Customer customer = new Customer(customerDTO.getCi(), customerDTO.getFirstName(),
                customerDTO.getLastName(), customerDTO.getTelephone());

        customerRepository.save(customer);
    }


    public void deleteCustomer(Integer customerId) throws CustomerNotExistException {
        boolean exists = customerRepository.existsById(customerId);
        if(!exists){
            throw new CustomerNotExistException("Cliente no existe.");
        }
        customerRepository.deleteById(customerId);
    }

    public List<Customer> customersSortedAlphabetically(String sort){
        if (sort.equalsIgnoreCase("desc")) {
            return customerRepository.findAll(Sort.by("lastName","firstName").descending());
        }
        return customerRepository.findAll(Sort.by("lastName","firstName"));
    }
}
