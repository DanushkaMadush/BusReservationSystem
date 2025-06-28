package application.services;

import domain.entities.Bus;
import domain.entities.Customer;
import domain.entities.Reservation;
import domain.interfaces.BusRepository;
import domain.interfaces.CustomerRepository;
import domain.interfaces.ReservationRepository;

import java.util.List;
import java.util.Optional;

public class ReservationService {

    private final BusRepository busRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(BusRepository busRepository,
                              CustomerRepository customerRepository,
                              ReservationRepository reservationRepository) {
        this.busRepository = busRepository;
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
    }

    // Book a seat for a customer
    public Optional<Reservation> reserveSeat(String mobileNumber, String busNumber, int seatNumber) {
        Optional<Customer> customerOpt = customerRepository.findByMobileNumber(mobileNumber);
        Optional<Bus> busOpt = busRepository.findByBusNumber(busNumber);

        if (customerOpt.isEmpty() || busOpt.isEmpty()) {
            return Optional.empty();
        }

        Bus bus = busOpt.get();
        if (!bus.isSeatAvailable(seatNumber)) {
            return Optional.empty();  // Seat already reserved
        }

        boolean reserved = bus.reserveSeat(seatNumber);
        if (!reserved) {
            return Optional.empty();
        }

        Reservation reservation = new Reservation(customerOpt.get(), bus, seatNumber);
        reservationRepository.save(reservation);

        System.out.println("Seat reserved successfully. Reservation ID: " + reservation.getReservationId());
        return Optional.of(reservation);
    }

    // Cancel a reservation
    public boolean cancelReservation(String reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);

        if (reservationOpt.isEmpty()) {
            return false;
        }

        Reservation reservation = reservationOpt.get();
        reservation.cancel();
        reservationRepository.save(reservation);
        reservation.getBus().cancelSeat(reservation.getSeatNumber());

        System.out.println("Reservation cancelled: " + reservationId);
        return true;
    }

    // List all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // List reservations for a customer
    public List<Reservation> getCustomerReservations(String mobileNumber) {
        return reservationRepository.findByCustomerMobile(mobileNumber);
    }
}
