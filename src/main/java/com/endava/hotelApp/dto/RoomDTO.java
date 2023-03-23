package com.endava.hotelApp.dto;

import com.endava.hotelApp.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomDTO {
    private int roomNumber;
    private int price;
    private RoomType roomType;

}
