package ru.adel.deliveryapp.services.impl;

import org.springframework.stereotype.Service;
import ru.adel.deliveryapp.models.Customer;
import ru.adel.deliveryapp.repositories.CustomerRepository;
import ru.adel.deliveryapp.services.CustomerService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
    }


    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = getCustomerById(id);
        return customerRepository.save(existingCustomer);
    }
    @Override
    public Optional<Customer> getCustomerByEmail(String email){
        return customerRepository.findCustomerByEmail(email);
    }
    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }



}

