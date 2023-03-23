package com.endava.hotelApp.controllers;

import com.endava.hotelApp.domain.Booking;
import com.endava.hotelApp.exception.*;
import com.endava.hotelApp.requests.BookingRequest;
import com.endava.hotelApp.services.IBookingService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/bookings")
public class BookingController {
    private static final Logger logger = LogManager.getLogger(BookingController.class);

    private final IBookingService bookingService;

    @DeleteMapping("/delete")
    public ResponseEntity deleteBooking(@RequestParam("id") Integer id){
        try {
            bookingService.deleteBooking(id);
        } catch (BookingNotExistException e) {
            logger.error("Error al eliminar reserva {}",id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/makeReservation")
    public ResponseEntity makeReservation(@RequestParam("ci") Integer ci,
                                                   @RequestBody BookingRequest bookingRequest) {
        Booking response = null;
        try {
            response = bookingService.makeReservation(ci,
                    bookingRequest.getCheckIn(),
                    bookingRequest.getCheckOut(),
                    bookingRequest.getRoomId());

        } catch (RoomNotExistException | CustomerNotExistException e) {
            logger.error("Error al hacer una reserva en el cuarto {} el cliente {}",bookingRequest.getRoomId(),ci,e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (RoomReservedException roomReserved) {
            logger.error("Error al hacer una reserva en el cuarto {} el cliente {}",bookingRequest.getRoomId(),ci,roomReserved);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roomReserved.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/cancelReservation/{bookingId}")
    public ResponseEntity cancelReservation(@PathVariable("bookingId") Integer bookingId,
                                                      @RequestParam Integer customerCi) {
        Booking response = null;
        try {
            response = bookingService.cancelReservation(customerCi, bookingId);
        } catch (BookingNotExistException notExistException) {
            logger.error("Fallo la reserva {}",bookingId,notExistException);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notExistException.getMessage());

        } catch (BookingInProgressException exception){
            logger.error("Fallo la reserva {}",bookingId,exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity getBookings(){
        List<Booking> bookings = bookingService.getBookings();

        return Objects.isNull(bookings)? ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay reservas.")
                : ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @GetMapping("/currentReserves")
    public ResponseEntity getCurrentReserves(){
        Map<Integer,List<Booking>> currentReserves = bookingService.currentReserves();

        return Objects.isNull(currentReserves)? ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay reservas.")
                : ResponseEntity.status(HttpStatus.OK).body(currentReserves);
    }

    @GetMapping("/generalState")
    public ResponseEntity getGeneralHotelState(@RequestParam String date, @RequestParam String date2) {
        List<String> generalState = bookingService.showGeneralState(LocalDate.parse(date), LocalDate.parse(date2));

        return Objects.isNull(generalState)? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.OK).body(generalState);
    }
}
