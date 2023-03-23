package com.endava.hotelApp;

import com.endava.hotelApp.domain.Booking;
import com.endava.hotelApp.domain.Customer;
import com.endava.hotelApp.domain.Luxury;
import com.endava.hotelApp.domain.Room;

import java.time.LocalDate;

public class BookingTestBase {

    protected Room validLuxuryRoom = new Luxury(100);
    protected Booking validBooking= new Booking(100, LocalDate.of(2023,1,1),
            LocalDate.of(2023,1,15), validLuxuryRoom);

    protected Customer customer = new Customer(51204985,"Analia","Perez", 999105864);

    protected String validBookingRequestJSON = "{\"checkIn\":\"2022-11-1\",\"checkOut\":\"2022-11-5\",\"roomId\":\"100\"}";
    protected String invalidBookingRequestJSON = "{\"checkIn\":\"2023-11-1\",\"checkOut\":\"2023-11-5\",\"roomId\":\"1000\"}";
    protected String invalidRequestRoomReservedJSON = "{\"checkIn\":\"2022-11-1\",\"checkOut\":\"2022-11-5\",\"roomId\":\"1\"}";

    public BookingTestBase() throws Exception {
    }
}
