import application.services.BusService;
import application.services.CustomerService;
import application.services.ReservationService;
import domain.entities.Bus;
import domain.entities.Reservation;
import infrastructure.repositories.InMemoryBusRepository;
import infrastructure.repositories.InMemoryCustomerRepository;
import infrastructure.repositories.InMemoryReservationRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize repositories
        InMemoryCustomerRepository customerRepo = new InMemoryCustomerRepository();
        InMemoryBusRepository busRepo = new InMemoryBusRepository();
        InMemoryReservationRepository reservationRepo = new InMemoryReservationRepository();

        // Initialize services
        CustomerService customerService = new CustomerService(customerRepo);
        BusService busService = new BusService(busRepo);
        ReservationService reservationService = new ReservationService(busRepo, customerRepo, reservationRepo);

        try (Scanner scanner = new Scanner(System.in)) {
            int choice;
            
            do {
                System.out.println("\n=== Bus Reservation System ===");
                System.out.println("1. Register Customer");
                System.out.println("2. Register Bus");
                System.out.println("3. Search Buses");
                System.out.println("4. Reserve Seat");
                System.out.println("5. Cancel Reservation");
                System.out.println("6. View All Reservations");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");
                choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> {
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Mobile: ");
                        String mobile = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("City: ");
                        String city = scanner.nextLine();
                        System.out.print("Age: ");
                        int age = Integer.parseInt(scanner.nextLine());
                        customerService.registerCustomer(name, mobile, email, city, age);
                    }
                    
                    case 2 -> {
                        System.out.print("Bus Number: ");
                        String busNum = scanner.nextLine();
                        System.out.print("Total Seats: ");
                        int totalSeats = Integer.parseInt(scanner.nextLine());
                        System.out.print("Start Point: ");
                        String start = scanner.nextLine();
                        System.out.print("End Point: ");
                        String end = scanner.nextLine();
                        System.out.print("Start Time (HH:mm): ");
                        LocalTime time = LocalTime.parse(scanner.nextLine());
                        System.out.print("Fare: ");
                        double fare = Double.parseDouble(scanner.nextLine());
                        busService.registerBus(busNum, totalSeats, start, end, time, fare);
                    }
                    
                    case 3 -> {
                        System.out.print("From: ");
                        String from = scanner.nextLine();
                        System.out.print("To: ");
                        String to = scanner.nextLine();
                        List<Bus> results = busService.searchBuses(from, to);
                        if (results.isEmpty()) {
                            System.out.println("No buses found.");
                        } else {
                            System.out.println("Buses found:");
                            for (Bus b : results) {
                                System.out.printf("Bus: %s | Time: %s | Fare: %.2f\n", b.getBusNumber(), b.getStartTime(), b.getFare());
                            }
                        }
                    }
                    
                    case 4 -> {
                        System.out.print("Your Mobile: ");
                        String mobile = scanner.nextLine();
                        System.out.print("Bus Number: ");
                        String busNum = scanner.nextLine();
                        System.out.print("Seat Number (0-based): ");
                        int seat = Integer.parseInt(scanner.nextLine());
                        Optional<Reservation> r = reservationService.reserveSeat(mobile, busNum, seat);
                        if (r.isPresent()) {
                            System.out.println("Reservation successful. ID: " + r.get().getReservationId());
                        } else {
                            System.out.println("Reservation failed.");
                        }
                    }
                    
                    case 5 -> {
                        System.out.print("Reservation ID to cancel: ");
                        String id = scanner.nextLine();
                        boolean cancelled = reservationService.cancelReservation(id);
                        System.out.println(cancelled ? "Cancelled successfully." : "Cancellation failed.");
                    }
                    
                    case 6 -> {
                        List<Reservation> all = reservationService.getAllReservations();
                        if (all.isEmpty()) {
                            System.out.println("No reservations yet.");
                        } else {
                            System.out.println("Reservations:");
                            for (Reservation res : all) {
                                System.out.printf("ID: %s | Customer: %s | Bus: %s | Seat: %d | Status: %s\n",
                                        res.getReservationId(),
                                        res.getCustomer().getName(),
                                        res.getBus().getBusNumber(),
                                        res.getSeatNumber(),
                                        res.getStatus());
                            }
                        }
                    }
                    
                    case 0 -> System.out.println("Exiting system. Goodbye!");
                    default -> System.out.println("Invalid choice.");
                }
                
            } while (choice != 0);
        }
    }
}
