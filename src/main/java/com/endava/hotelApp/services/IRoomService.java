package com.endava.hotelApp.services;

import com.endava.hotelApp.domain.Booking;
import com.endava.hotelApp.domain.Room;
import com.endava.hotelApp.dto.RoomDTO;
import com.endava.hotelApp.exception.RoomExistException;
import com.endava.hotelApp.exception.RoomNotExistException;

import java.util.List;

public interface IRoomService {
    List<Room> getRooms();
    void addRoom(RoomDTO room) throws RoomExistException;
    void deleteRoom(Integer id) throws RoomNotExistException;
    List<Booking> reservationHistoryByRoom(Integer roomId);
}
