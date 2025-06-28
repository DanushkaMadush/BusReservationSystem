package domain.interfaces;

import domain.entities.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    // Save a new reservation
    void save(Reservation reservation);

    // Find reservation by reservation ID
    Optional<Reservation> findById(String reservationId);

    // Find all reservations
    List<Reservation> findAll();

    // Find all reservations by customer (using customer mobile or ID)
    List<Reservation> findByCustomerMobile(String mobileNumber);

    // Find all reservations by bus number
    List<Reservation> findByBusNumber(String busNumber);

    // Delete a reservation by ID (e.g., on cancellation)
    boolean deleteById(String reservationId);
}
