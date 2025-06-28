package application.services;

import domain.entities.Bus;
import domain.interfaces.BusRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class BusService {

    private final BusRepository busRepository;

    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    // Register a new bus
    public boolean registerBus(String busNumber, int totalSeats, String startPoint,
                               String endPoint, LocalTime startTime, double fare) {
        Optional<Bus> existingBus = busRepository.findByBusNumber(busNumber);
        if (existingBus.isPresent()) {
            System.out.println("Bus already registered with number: " + busNumber);
            return false;
        }

        Bus bus = new Bus(busNumber, totalSeats, startPoint, endPoint, startTime, fare);
        busRepository.save(bus);
        System.out.println("Bus registered successfully: " + busNumber);
        return true;
    }

    // Search buses by route
    public List<Bus> searchBuses(String startPoint, String endPoint) {
        return busRepository.findByRoute(startPoint, endPoint);
    }

    // Get all buses
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    // Find a bus by number (optional)
    public Optional<Bus> getBusByNumber(String busNumber) {
        return busRepository.findByBusNumber(busNumber);
    }
}
