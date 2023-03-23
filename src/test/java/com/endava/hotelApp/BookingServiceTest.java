package com.endava.hotelApp;


import com.endava.hotelApp.exception.BookingExistException;
import com.endava.hotelApp.exception.BookingNotExistException;
import com.endava.hotelApp.repositories.BookingRepository;
import com.endava.hotelApp.repositories.CustomerRepository;
import com.endava.hotelApp.repositories.RoomRepository;
import com.endava.hotelApp.services.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class BookingServiceTest extends BookingTestBase{
    @Autowired
    BookingService bookingService;

    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private RoomRepository roomRepository;
    @MockBean
    private CustomerRepository customerRepository;

    public BookingServiceTest() throws Exception {
    }

    @Test
    public void deleteBookingShouldThrowAnExceptionDueToBookingNotFound(){
        when(bookingRepository.existsById(any())).thenReturn(false);

        assertThrows(BookingNotExistException.class, () -> bookingService.deleteBooking(10000));
    }

    @Test
    public void makeReservationShouldThrowAnExceptionDueToRoomNotFound() throws Exception {
        when(roomRepository.findById(100)).thenReturn(Optional.ofNullable(validLuxuryRoom));
        when(customerRepository.findById(52204945)).thenReturn(Optional.ofNullable(customer));

        assertThrows(BookingExistException.class,
                () -> assertNotNull(bookingService.makeReservation(52204945,"2022-07-10","2022-07-15",100)));

    }

}