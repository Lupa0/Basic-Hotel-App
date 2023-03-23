package com.endava.hotelApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerDTO {
    private Integer ci;
    private String firstName;
    private String lastName;
    private Integer telephone;

}
