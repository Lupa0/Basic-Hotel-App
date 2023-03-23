package com.endava.hotelApp.requests;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {
    private String checkIn;
    private String checkOut;
    private Integer roomId;

}
