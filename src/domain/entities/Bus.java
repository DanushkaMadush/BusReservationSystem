package domain.entities;

import java.time.LocalTime;
import java.util.Arrays;

public class Bus {
    private final String busNumber;
    private final int totalSeats;
    private final String startPoint;
    private final String endPoint;
    private final LocalTime startTime;
    private final double fare;
    private final boolean[] seatAvailability; // true = available, false = reserved

    // Constructor
    public Bus(String busNumber, int totalSeats, String startPoint, String endPoint, LocalTime startTime, double fare) {
        this.busNumber = busNumber;
        this.totalSeats = totalSeats;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startTime = startTime;
        this.fare = fare;
        this.seatAvailability = new boolean[totalSeats];
        Arrays.fill(this.seatAvailability, true); // All seats available initially
    }

    // Getters
    public String getBusNumber() {
        return busNumber;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public double getFare() {
        return fare;
    }

    public boolean[] getSeatAvailability() {
        return seatAvailability;
    }

    // Check if a seat is available
    public boolean isSeatAvailable(int seatNumber) {
        if (seatNumber < 0 || seatNumber >= totalSeats) {
            return false;
        }
        return seatAvailability[seatNumber];
    }

    // Reserve a seat
    public boolean reserveSeat(int seatNumber) {
        if (isSeatAvailable(seatNumber)) {
            seatAvailability[seatNumber] = false;
            return true;
        }
        return false;
    }

    // Cancel a seat reservation
    public boolean cancelSeat(int seatNumber) {
        if (seatNumber >= 0 && seatNumber < totalSeats && !seatAvailability[seatNumber]) {
            seatAvailability[seatNumber] = true;
            return true;
        }
        return false;
    }
}
