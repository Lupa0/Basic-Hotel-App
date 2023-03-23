package com.endava.hotelApp.domain;

import com.endava.hotelApp.enums.Services;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor
@Entity
@DiscriminatorValue("standard")
public class Standard extends Room{
    public Standard(Integer roomNumber) {
        super(roomNumber);
        this.pricePerNight = 3000;
        this.service = Services.ROOM_SERVICE.name() + ", " + Services.CLEANING.name();
    }

    public Standard(Integer roomNumber, Integer pricePerNight) {
        super(roomNumber, pricePerNight);
        this.service = Services.ROOM_SERVICE.name() + ", " + Services.CLEANING.name();
    }


    @Override
    public String toString() {
        return "Standard Room " + getRoomNumber() + ", cost: $" + pricePerNight;
    }
}
