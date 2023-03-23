package com.endava.hotelApp.controllers;

import com.endava.hotelApp.domain.Customer;
import com.endava.hotelApp.dto.CustomerDTO;
import com.endava.hotelApp.exception.CustomerExistException;
import com.endava.hotelApp.exception.CustomerNotExistException;
import com.endava.hotelApp.services.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/customers")
public class CustomerController {
    private static final Logger logger = LogManager.getLogger(CustomerController.class);

    private final ICustomerService customerService;

    @PostMapping
    public ResponseEntity createCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            customerService.addCustomer(customerDTO);
        } catch (CustomerExistException e) {
            logger.error("Error al crear cliente {}",customerDTO.getCi(),e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
    }

    @GetMapping("/sortByName")
    public ResponseEntity getSortedCustomers(@RequestParam(required = false, defaultValue = "asc") String sort){
        List<Customer> customers = customerService.customersSortedAlphabetically(sort);

        return Objects.isNull(customers)? ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay clientes.")
                : ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @DeleteMapping("/{ci}")
    public ResponseEntity deleteCustomer(@PathVariable("ci") Integer id){
        try {
            customerService.deleteCustomer(id);
        } catch (CustomerNotExistException e) {
            logger.error("Error al eliminar cliente {}",id,e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }



}
