package com.endava.hotelApp.domain;

import com.endava.hotelApp.enums.BookingState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@NoArgsConstructor
@Getter
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Integer bookingId;

    @Setter
    private LocalDate checkIn;
    @Setter
    private LocalDate checkOut;
    private Long days;
    private Double finalPrice;

    @Enumerated(EnumType.STRING)
    @Setter
    private BookingState bookingState;

    @ManyToOne
    @JoinColumn(name = "bookings_room", referencedColumnName = "roomNumber")
    @Setter
    private Room room;


    public Booking(Integer bookingId, LocalDate checkIn, LocalDate checkOut, Room room) {
        this.bookingId = bookingId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.room = room;
        this.bookingState = BookingState.BOOKED;
        setDays(checkIn,checkOut);
        setFinalPrice();
    }

    public Booking(LocalDate checkIn, LocalDate checkOut, Room room){
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        setDays(checkIn,checkOut);
        this.room = room;
        setFinalPrice();
        this.bookingState = BookingState.BOOKED;
    }

    public void setDays(LocalDate checkIn, LocalDate checkOut){
        this.days = DAYS.between(checkIn, checkOut);
    }

    public void setFinalPrice() {
        try {
            this.finalPrice = calculatePrice(this.days);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private double calculatePrice(Long days) throws Exception {
        if(days < 1) {
            throw new Exception("Es necesario reservar minimo una noche.");
        } else {
            if(days > 7){
                if(this.room instanceof Luxury){
                    return this.room.getPricePerNight()*days - (this.room.getPricePerNight()*days)*0.10;
                } else {
                    return this.room.getPricePerNight()*days - (this.room.getPricePerNight()*days)*0.5;
                }
            } else {
                return this.room.getPricePerNight()*days;
            }
        }
    }




}
