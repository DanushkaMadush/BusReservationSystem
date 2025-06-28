package domain.interfaces;

import domain.entities.Bus;
import java.util.List;
import java.util.Optional;

public interface BusRepository {

    // Save a new bus or update existing
    void save(Bus bus);

    // Find a bus by its bus number (unique)
    Optional<Bus> findByBusNumber(String busNumber);

    // Find all buses
    List<Bus> findAll();

    // Delete a bus by bus number
    boolean deleteByBusNumber(String busNumber);
    
    // Find buses by route (start and end point)
    List<Bus> findByRoute(String startPoint, String endPoint);
}
