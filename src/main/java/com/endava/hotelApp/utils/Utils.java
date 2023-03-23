package com.endava.hotelApp.utils;

import com.endava.hotelApp.domain.Customer;
import com.endava.hotelApp.domain.Luxury;
import com.endava.hotelApp.domain.Room;
import com.endava.hotelApp.domain.Standard;
import com.endava.hotelApp.dto.CustomerDTO;
import com.endava.hotelApp.dto.RoomDTO;
import com.endava.hotelApp.enums.RoomType;

public class Utils {
    public static Room mapDTOToRoom(RoomDTO roomDTO) {
        final Room room;
        if(roomDTO.getRoomType().equals(RoomType.LUXURY)){
            room = new Luxury(roomDTO.getRoomNumber(),roomDTO.getPrice());
        } else {
            room = new Standard(roomDTO.getRoomNumber(),roomDTO.getPrice());
        }
        return room;
    }


}
