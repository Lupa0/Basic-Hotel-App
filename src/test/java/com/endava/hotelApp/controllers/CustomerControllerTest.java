package com.endava.hotelApp.controllers;

import com.endava.hotelApp.dto.CustomerDTO;
import com.endava.hotelApp.exception.CustomerExistException;
import com.endava.hotelApp.exception.CustomerNotExistException;
import com.endava.hotelApp.services.ICustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public ICustomerService customerService;

    private static String route = "/api/customers";

    private CustomerDTO customerDTO = new CustomerDTO(41203948,"Jhon","Doe",69836285);
    private String requestJSON = "{\"ci\":\"41203948\",\"firstName\":\"Jhon\",\"lastName\":\"Doe\",\"telephone\":\"69836285\"}";


    @Test
    void createCustomerWithSuccess() throws Exception {
        doNothing().when(customerService).addCustomer(customerDTO);

        mockMvc.perform(post(route).contentType(MediaType.APPLICATION_JSON)
                .content(requestJSON)).andExpect(status().isCreated());
    }

    @Test
    void createCustomerShouldThrowAnErrorCustomerExists() throws Exception {
        doThrow(CustomerExistException.class).when(customerService).addCustomer(any());

        mockMvc.perform(post(route).contentType(MediaType.APPLICATION_JSON)
                .content(requestJSON)).andExpect(status().isBadRequest());
    }

    @Test
    void deleteCustomerWithSuccess() throws Exception {
        doNothing().when(customerService).deleteCustomer(41203948);

        mockMvc.perform(delete(route + "/41203948")).andExpect(status().isOk());
    }

    @Test
    void deleteCustomerShouldThrowAnErrorCustomerNotExists() throws Exception {
        doThrow(CustomerNotExistException.class).when(customerService).deleteCustomer(41203948);

        mockMvc.perform(delete(route + "/41203948")).andExpect(status().isNotFound());
    }
}