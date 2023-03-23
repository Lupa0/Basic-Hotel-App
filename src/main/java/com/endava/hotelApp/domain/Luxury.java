package com.endava.hotelApp.domain;

import com.endava.hotelApp.enums.Services;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor
@Entity
@DiscriminatorValue("luxury")
public class Luxury extends Room{

    private boolean hasMiniBar;
    private boolean hasSafeBox;

    public Luxury(Integer roomNumber) {
        super(roomNumber);
        this.hasMiniBar = true;
        this.hasSafeBox = true;
        this.pricePerNight = 5000;
        this.service = Services.ROOM_SERVICE.name() + ", " + Services.CLEANING.name() + ", " + Services.LAUNDRY.name();
    }

    public Luxury(Integer roomNumber, Integer pricePerNight) {
        super(roomNumber, pricePerNight);
        this.hasMiniBar = true;
        this.hasSafeBox = true;
        this.service = Services.ROOM_SERVICE.name() + ", " + Services.CLEANING.name() + ", " + Services.LAUNDRY.name();
    }


    @Override
    public String toString() {
        return "Luxury Room " + getRoomNumber() + ", cost: $" + pricePerNight;
    }
}
