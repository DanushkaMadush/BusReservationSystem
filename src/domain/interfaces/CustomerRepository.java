package domain.interfaces;

import domain.entities.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    // Save a new customer or update existing
    void save(Customer customer);

    // Find customer by mobile number (unique)
    Optional<Customer> findByMobileNumber(String mobileNumber);

    // Find customer by email (unique)
    Optional<Customer> findByEmail(String email);

    // Find all customers
    List<Customer> findAll();

    // Delete a customer by mobile number
    boolean deleteByMobileNumber(String mobileNumber);
}
