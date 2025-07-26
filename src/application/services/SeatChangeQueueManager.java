package application.services;

import domain.entities.Reservation;
import java.util.*;

public class SeatChangeQueueManager {

    // Map<busNumber, Map<seatNumber, Queue<Reservation>>>
    private final Map<String, Map<Integer, Queue<Reservation>>> changeRequests = new HashMap<>();

    // Add a seat change request
    public void requestSeatChange(Reservation reservation, int desiredSeatNumber) {
        String busNumber = reservation.getBus().getBusNumber();
        changeRequests
            .computeIfAbsent(busNumber, k -> new HashMap<>())
            .computeIfAbsent(desiredSeatNumber, k -> new LinkedList<>())
            .add(reservation);

        System.out.println("Customer " + reservation.getCustomer().getName()
                + " added to waiting list for seat " + desiredSeatNumber + " on bus " + busNumber);
    }

    // Process seat availability (called after a seat becomes free)
    public Optional<Reservation> assignToNextInQueue(String busNumber, int seatNumber) {
        Map<Integer, Queue<Reservation>> seatMap = changeRequests.get(busNumber);
        if (seatMap == null || !seatMap.containsKey(seatNumber)) {
            return Optional.empty();
        }

        Queue<Reservation> queue = seatMap.get(seatNumber);
        if (queue.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(queue.poll()); // Return first waiting reservation
    }

    // Check if a customer is already in a queue
    public boolean isAlreadyInQueue(Reservation reservation, int seatNumber) {
        String busNumber = reservation.getBus().getBusNumber();
        Map<Integer, Queue<Reservation>> seatMap = changeRequests.get(busNumber);
        if (seatMap == null || !seatMap.containsKey(seatNumber)) {
            return false;
        }
        return seatMap.get(seatNumber).contains(reservation);
    }
}
