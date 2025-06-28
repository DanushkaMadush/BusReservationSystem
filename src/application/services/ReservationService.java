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

    private final SeatChangeQueueManager queueManager = new SeatChangeQueueManager();

    // Cancel a reservation
    public boolean cancelReservation(String reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);

        if (reservationOpt.isEmpty()) {
            return false;
        }

        Reservation reservation = reservationOpt.get();
        reservation.cancel();
        reservationRepository.save(reservation);
        Bus bus = reservation.getBus();
        int seat = reservation.getSeatNumber();

        // Mark seat available
        bus.cancelSeat(seat);

        // Assign seat to next customer in queue (if any)
        Optional<Reservation> waiting = queueManager.assignToNextInQueue(bus.getBusNumber(), seat);
        if (waiting.isPresent()) {
            Reservation next = waiting.get();
            boolean success = bus.reserveSeat(seat);
            if (success) {
                Reservation updated = new Reservation(next.getCustomer(), bus, seat);
                reservationRepository.save(updated);
                System.out.println("Seat " + seat + " assigned to waiting customer: " + next.getCustomer().getName());
            }
        }

        System.out.println("Reservation cancelled: " + reservationId);
        return true;
    }

    // List reservations for a customer
    public List<Reservation> getCustomerReservations(String mobileNumber) {
        return reservationRepository.findByCustomerMobile(mobileNumber);
    }

    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }

    public boolean requestSeatChange(Reservation reservation, int desiredSeat) {
        if (!reservation.getBus().isSeatAvailable(desiredSeat)) {
            queueManager.requestSeatChange(reservation, desiredSeat);
            return true;
        }
        return false; // seat already available, no need to queue
    }

    // List all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

}
