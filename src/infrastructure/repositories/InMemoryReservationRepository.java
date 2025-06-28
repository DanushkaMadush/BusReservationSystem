package infrastructure.repositories;

import domain.entities.Reservation;
import domain.interfaces.ReservationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public void save(Reservation reservation) {
        // If reservation with same ID exists, replace it
        deleteById(reservation.getReservationId());
        reservations.add(reservation);
    }

    @Override
    public Optional<Reservation> findById(String reservationId) {
        return reservations.stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst();
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations); // Defensive copy
    }

    @Override
    public List<Reservation> findByCustomerMobile(String mobileNumber) {
        return reservations.stream()
                .filter(r -> r.getCustomer().getMobileNumber().equals(mobileNumber))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByBusNumber(String busNumber) {
        return reservations.stream()
                .filter(r -> r.getBus().getBusNumber().equals(busNumber))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(String reservationId) {
        return reservations.removeIf(r -> r.getReservationId().equals(reservationId));
    }
}
