package com.endava.hotelApp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Entity
@Table
public class Customer implements Comparable<Customer>{
    @Id
    @Setter
    private Integer ci;

    @Setter
    private String firstName;

    @Setter
    private  String lastName;

    @Setter
    private Integer telephone;

    @Setter
    private boolean ableToBook;

    @OneToMany
    @JoinTable(name = "customer_reserve",
                joinColumns = @JoinColumn(name = "customer", referencedColumnName = "ci"),
                inverseJoinColumns = @JoinColumn(name = "booking", referencedColumnName = "bookingId"))
    private Map<Integer,Booking> reservation;

    public Customer(Integer ci,String firstName, String lastName, Integer telephone) {
        this.ci = ci;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.ableToBook = true;
        this.reservation = new HashMap<>();
    }

    public void setReservation(Booking reservation) {
        this.reservation.put(reservation.getBookingId(), reservation);
    }

    @Override
    public String toString() {
        return "CI: " + this.ci + "\nNombre: " + this.firstName + "\nApellido: " + this.lastName + "\nTelefono: " + this.telephone + "\n";
    }

    @Override
    public int compareTo(Customer o) {
        if(this.lastName.compareTo(o.lastName) == 0)
        {
            return this.firstName.compareTo(o.getFirstName());
        }else {
            return this.lastName.compareTo(o.getLastName());
        }
    }
}
