package infrastructure.repositories;

import domain.entities.Bus;
import domain.interfaces.BusRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryBusRepository implements BusRepository {

    private final List<Bus> buses = new ArrayList<>();

    @Override
    public void save(Bus bus) {
        // Remove existing bus with same busNumber before adding
        buses.removeIf(b -> b.getBusNumber().equals(bus.getBusNumber()));
        buses.add(bus);
    }

    @Override
    public Optional<Bus> findByBusNumber(String busNumber) {
        return buses.stream()
                .filter(b -> b.getBusNumber().equals(busNumber))
                .findFirst();
    }

    @Override
    public List<Bus> findAll() {
        return new ArrayList<>(buses);
    }

    @Override
    public boolean deleteByBusNumber(String busNumber) {
        return buses.removeIf(b -> b.getBusNumber().equals(busNumber));
    }

    @Override
    public List<Bus> findByRoute(String startPoint, String endPoint) {
        return buses.stream()
                .filter(b -> b.getStartPoint().equalsIgnoreCase(startPoint)
                          && b.getEndPoint().equalsIgnoreCase(endPoint))
                .collect(Collectors.toList());
    }
}
