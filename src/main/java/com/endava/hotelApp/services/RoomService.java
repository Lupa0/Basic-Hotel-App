package com.endava.hotelApp.services;

import com.endava.hotelApp.domain.Booking;
import com.endava.hotelApp.domain.Room;
import com.endava.hotelApp.dto.RoomDTO;
import com.endava.hotelApp.exception.RoomExistException;
import com.endava.hotelApp.exception.RoomNotExistException;
import com.endava.hotelApp.repositories.BookingRepository;
import com.endava.hotelApp.repositories.RoomRepository;
import com.endava.hotelApp.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoomService implements IRoomService{
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;


    public List<Room> getRooms(){
        return roomRepository.findAll();
    }

    public void addRoom(RoomDTO room) throws RoomExistException {
        boolean exist = roomRepository.existsById(room.getRoomNumber());
        if(exist){
            throw new RoomExistException("Cuarto " + room.getRoomNumber() + " ya existe.");
        }
        saveRoom(room);
    }

    private void saveRoom(RoomDTO roomDTO){
        final Room room = Utils.mapDTOToRoom(roomDTO);
        roomRepository.save(room);
    }

    public void deleteRoom(Integer id) throws RoomNotExistException {
        boolean exist = roomRepository.existsById(id);
        if(!exist){
            throw new RoomNotExistException("Cuarto " + id + "no existe.");
        }
        roomRepository.deleteById(id);
    }

    public List<Booking> reservationHistoryByRoom(Integer roomId){
        return bookingRepository.findAll().stream()
                .filter(b -> b.getRoom().getRoomNumber().equals(roomId))
                .collect(Collectors.toList());
    }
}
