package domain.entities;

import java.util.UUID;

public class Reservation {
    private final String reservationId;  // Unique reservation ID
    private final Customer customer;
    private final Bus bus;
    private final int seatNumber;
    private ReservationStatus status;

    // Enum for reservation status
    public enum ReservationStatus {
        ACTIVE,
        CANCELLED
    }

    // Constructor
    public Reservation(Customer customer, Bus bus, int seatNumber) {
        this.reservationId = UUID.randomUUID().toString();  // Generate unique ID
        this.customer = customer;
        this.bus = bus;
        this.seatNumber = seatNumber;
        this.status = ReservationStatus.ACTIVE;
    }

    // Getters
    public String getReservationId() {
        return reservationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Bus getBus() {
        return bus;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    // Cancel reservation
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }
}
