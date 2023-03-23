package com.endava.hotelApp.services;

import com.endava.hotelApp.domain.Booking;
import com.endava.hotelApp.exception.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IBookingService {
    List<Booking> getBookings();
    void addBooking(Booking booking) throws BookingExistException;
    void deleteBooking(Integer bookingId) throws BookingNotExistException;
    Booking cancelReservation(Integer userCi, Integer bookingNumber) throws BookingInProgressException, BookingNotExistException;
    Booking makeReservation(Integer user, String entrance, String leaving, Integer roomId)
            throws RoomNotExistException, CustomerNotExistException, RoomReservedException;
    Map<Integer,List<Booking>> currentReserves();
    List<String> showGeneralState(LocalDate date1, LocalDate date2);

}
