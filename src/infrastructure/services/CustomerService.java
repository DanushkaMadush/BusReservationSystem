package application.services;

import domain.entities.Customer;
import domain.interfaces.CustomerRepository;

import java.util.List;
import java.util.Optional;

public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Register a new customer
    public boolean registerCustomer(String name, String mobileNumber, String email, String city, int age) {
        // Prevent duplicate registration
        Optional<Customer> existing = customerRepository.findByMobileNumber(mobileNumber);
        if (existing.isPresent()) {
            System.out.println("Customer already exists with mobile: " + mobileNumber);
            return false;
        }

        Customer customer = new Customer(name, mobileNumber, email, city, age);
        customerRepository.save(customer);
        System.out.println("Customer registered successfully: " + name);
        return true;
    }

    // Retrieve a customer by mobile number
    public Optional<Customer> getCustomer(String mobileNumber) {
        return customerRepository.findByMobileNumber(mobileNumber);
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
