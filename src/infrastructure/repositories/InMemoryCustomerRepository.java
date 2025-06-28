package infrastructure.repositories;

import domain.entities.Customer;
import domain.interfaces.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final List<Customer> customers = new ArrayList<>();

    @Override
    public void save(Customer customer) {
        // Remove existing customer with same mobile or email before adding new
        customers.removeIf(c -> c.getMobileNumber().equals(customer.getMobileNumber()) 
                             || c.getEmail().equals(customer.getEmail()));
        customers.add(customer);
    }

    @Override
    public Optional<Customer> findByMobileNumber(String mobileNumber) {
        return customers.stream()
                .filter(c -> c.getMobileNumber().equals(mobileNumber))
                .findFirst();
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customers.stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    @Override
    public boolean deleteByMobileNumber(String mobileNumber) {
        return customers.removeIf(c -> c.getMobileNumber().equals(mobileNumber));
    }
}
