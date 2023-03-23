package com.endava.hotelApp.services;

import com.endava.hotelApp.domain.Booking;
import com.endava.hotelApp.domain.Customer;
import com.endava.hotelApp.domain.Room;
import com.endava.hotelApp.enums.BookingState;
import com.endava.hotelApp.exception.*;
import com.endava.hotelApp.repositories.BookingRepository;
import com.endava.hotelApp.repositories.CustomerRepository;
import com.endava.hotelApp.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.endava.hotelApp.utils.DateUtils.*;

@RequiredArgsConstructor
@Service
public class BookingService implements IBookingService{
    private static final Logger logger = LogManager.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    public List<Booking> getBookings(){
        return bookingRepository.findAll();
    }

    public void addBooking(Booking booking) throws BookingExistException {
        boolean exists = bookingRepository.existsById(booking.getBookingId());
        if(exists){
            throw new BookingExistException("La reserva ya existe");
        }
        bookingRepository.save(booking);
    }

    public void deleteBooking(Integer bookingId) throws BookingNotExistException {
        boolean exists = bookingRepository.existsById(bookingId);
        if(!exists){
            throw new BookingNotExistException("Reserva " + bookingId + " no existe");
        }
        bookingRepository.deleteById(bookingId);
    }

    public Booking cancelReservation(Integer userCi, Integer bookingNumber)
            throws BookingInProgressException, BookingNotExistException {
        Optional<Customer> user = customerRepository.findById(userCi);
        Booking booking = user.get().getReservation().get(bookingNumber);

        if(booking != null){
            if(booking.getCheckIn().isAfter(LocalDate.now())) {
                booking.setBookingState(BookingState.CANCELLED);
                bookingRepository.save(booking);

                user.get().getReservation().remove(booking.getBookingId());
                customerRepository.save(user.get());

                logger.info("Se ha cancelado la reserva en el cuarto {} con exito. Para fecha {}.",
                        booking.getRoom().getRoomNumber(),booking.getCheckIn());
                return booking;
            } else throw new BookingInProgressException("No es posible cancelar. Reserva en progreso");
        } else throw new BookingNotExistException("Reserva no encontrada.");

    }
    private boolean isReserved(Booking booking, LocalDate checkIn, LocalDate checkOut){
        return ((isAfterOrEqual(booking.getCheckIn(), checkIn) && isBeforeOrEqual(booking.getCheckIn(),checkOut)) ||
                (isAfterOrEqual(booking.getCheckOut(), checkIn) && isBeforeOrEqual(booking.getCheckOut(),checkOut)) ||
                (isBeforeOrEqual(booking.getCheckIn(),checkIn) && (isAfterOrEqual(booking.getCheckOut(), checkOut))));

    }
    private Set<Room> roomsReserved(LocalDate entrance, LocalDate leaving){
        Set<Room> roomsReserved = new HashSet<>();
        List<Booking> booked = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingState().equals(BookingState.BOOKED))
                .collect(Collectors.toList());

        for(Booking b : booked){
            if(entrance.isAfter(LocalDate.now())){
                if(isReserved(b,entrance,leaving)){
                    roomsReserved.add(b.getRoom());
                }
            }
        }
        return roomsReserved;
    }
    private Set<Room> roomsAvailable(LocalDate entrance, LocalDate leaving){
        Set<Room> roomsAvailable = new HashSet<>();
        Set<Room> roomsReserved = roomsReserved(entrance, leaving);

        if (roomsReserved.isEmpty()) {
            roomsAvailable.addAll(roomRepository.findAll());
        } else {
            for (Room r : roomRepository.findAll()) {
                if (!roomsReserved.contains(r)) {
                    roomsAvailable.add(r);
                }
            }
        }
        return roomsAvailable;
    }

    public Booking makeReservation(Integer user, String entrance, String leaving, Integer roomId)
            throws RoomNotExistException, CustomerNotExistException, RoomReservedException {

        Optional<Room> suite = roomRepository.findById(roomId);
        Booking reserve;
        Optional<Customer> customer = customerRepository.findById(user);

        if(suite.isEmpty()){
            throw new RoomNotExistException("Cuarto no encontrado!");
        }
        if(customer.isEmpty()){
            throw new CustomerNotExistException("Persona no encontrada!");
        }

        reserve = new Booking(LocalDate.parse(entrance), LocalDate.parse(leaving), suite.get());

        if(!roomsAvailable(LocalDate.parse(entrance), LocalDate.parse(leaving)).contains(suite.get())){
            throw new RoomReservedException("Cuarto ya esta reservado.");
        }

        bookingRepository.save(reserve);
        customer.get().setReservation(reserve);
        customerRepository.save(customer.get());
        logger.info("Se ha realizado la reserva en el cuarto {} con exito. Fecha de Check In : {}.",
                suite.get().getRoomNumber(),entrance);

        return reserve;
    }

    public Map<Integer,List<Booking>> currentReserves(){
        Map<Integer, List<Booking>> currentReserves = new HashMap<>();

        for(Booking b : bookingRepository.findAll()){
            int roomNumber = b.getRoom().getRoomNumber();
            if(!currentReserves.containsKey(roomNumber)){
                List<Booking> reserves = new ArrayList<>();
                currentReserves.put(roomNumber, reserves);
            }

            if(b.getCheckIn().isAfter(LocalDate.now())){
                currentReserves.get(roomNumber).add(b);
            }
        }
        return currentReserves;
    }
    public List<String> showGeneralState(LocalDate date1, LocalDate date2) {
        List<String> generalState = new ArrayList<>();
        generalState.add("Reserved rooms:");
        for (Customer customer : customerRepository.findAll()){
            List<Booking> list = customer.getReservation().values().stream()
                    .filter(booking -> isReserved(booking,date1,date2)).collect(Collectors.toList());

            for (Booking booking : list){
                generalState.add(booking.getRoom() + " Check In: " + booking.getCheckIn() + " Check Out: "
                        + booking.getCheckOut() + " by Client " + customer.getFirstName() + " " + customer.getLastName());
            }

        }
        generalState.add("Available rooms:");
        for(Room room : roomsAvailable(date1,date2)){
            generalState.add(room.toString());
        }
        return generalState;
    }
}
