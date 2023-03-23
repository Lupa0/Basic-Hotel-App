package com.endava.hotelApp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorColumn(name = "room")
public abstract class Room {
    @Id
    protected Integer roomNumber;
    protected boolean hasWifi;
    protected Integer pricePerNight;
    protected String service;

    public Room(Integer roomNumber) {
        this.roomNumber = roomNumber;
        this.hasWifi = true;
    }

    public Room(Integer roomNumber, Integer pricePerNight) {
        this.roomNumber = roomNumber;
        this.hasWifi = true;
        this.pricePerNight = pricePerNight;
    }

    public abstract String toString();
}
