package com.endava.hotelApp.exception;

public class BookingInProgressException extends Exception{
    public BookingInProgressException(String message) {
        super(message);
    }
}
