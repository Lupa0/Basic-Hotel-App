package com.endava.hotelApp;

import com.endava.hotelApp.exception.BookingExistException;
import com.endava.hotelApp.exception.BookingNotExistException;
import com.endava.hotelApp.exception.RoomNotExistException;
import com.endava.hotelApp.exception.RoomReservedException;
import com.endava.hotelApp.services.BookingService;
import com.endava.hotelApp.services.IBookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest extends BookingTestBase{
    @Autowired
    public MockMvc mockMvc;
    @MockBean
    public IBookingService bookingService;

    private static String route = "/api/bookings";

    BookingControllerTest() throws Exception {
    }

    @Test
    void makeReservationWithSuccess() throws Exception {
        when(bookingService.makeReservation(any(),any(),any(),any())).thenReturn(validBooking);

        mockMvc.perform(post(route + "/makeReservation").param("ci","51204985")
                .contentType(MediaType.APPLICATION_JSON).content(validBookingRequestJSON))
                .andExpect(status().isCreated());
    }

    @Test
    void makeReservationShouldThrowAnErrorDueToRoomNotFound() throws Exception {
        when(bookingService.makeReservation(any(),any(),any(),any()))
                .thenThrow(RoomNotExistException.class);

        mockMvc.perform(post(route + "/makeReservation").param("ci","51204985")
                .contentType(MediaType.APPLICATION_JSON).content(invalidBookingRequestJSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    void makeReservationShouldThrowAnErrorDueToRoomAlreadyReserved() throws Exception {
        when(bookingService.makeReservation(any(),any(),any(),any()))
                .thenThrow(RoomReservedException.class);

        mockMvc.perform(post(route + "/makeReservation").param("ci","51204985")
                .contentType(MediaType.APPLICATION_JSON).content(invalidRequestRoomReservedJSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void cancelReservationWithSuccess() throws Exception {
        when(bookingService.cancelReservation(52204945,100)).thenReturn(validBooking);

        mockMvc.perform(put(route + "/cancelReservation/100").param("customerCi","52204945"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void cancelReservationShouldThrowAnErrorDueToReserveNotFound() throws Exception {
        when(bookingService.cancelReservation(any(),any()))
                .thenThrow(BookingNotExistException.class);

        mockMvc.perform(put(route + "/cancelReservation/999999999").param("customerCi","52204945"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }



}