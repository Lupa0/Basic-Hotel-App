package com.endava.hotelApp.controllers;

import com.endava.hotelApp.domain.Booking;
import com.endava.hotelApp.domain.Room;
import com.endava.hotelApp.dto.RoomDTO;
import com.endava.hotelApp.exception.RoomExistException;
import com.endava.hotelApp.exception.RoomNotExistException;
import com.endava.hotelApp.services.IRoomService;
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
@RequestMapping("api/rooms")
public class RoomController {
    private static final Logger logger = LogManager.getLogger(RoomController.class);

    private final IRoomService roomService;

    @PostMapping
    public ResponseEntity createRoom(@RequestBody RoomDTO room){
        try {
            roomService.addRoom(room);
        } catch (RoomExistException e) {
            logger.error("Error al crear cuarto {}", room.getRoomNumber(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @GetMapping
    public ResponseEntity getRooms(){
        List<Room> rooms = roomService.getRooms();

        return Objects.isNull(rooms)? ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay cuartos.")
                : ResponseEntity.status(HttpStatus.OK).body(rooms);
    }

    @DeleteMapping("/{roomNumber}")
    public ResponseEntity deleteRoom(@PathVariable("roomNumber") Integer id){
        try {
            roomService.deleteRoom(id);
        } catch (RoomNotExistException e) {
            logger.error("Error al eliminar cuarto {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/getBookings/{roomId}")
    public ResponseEntity getBookingsByRoom(@PathVariable("roomId") Integer roomId){
        List<Booking> bookings = roomService.reservationHistoryByRoom(roomId);

        return Objects.isNull(bookings)? ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay reservas.")
                : ResponseEntity.status(HttpStatus.OK).body(bookings);
    }
}
