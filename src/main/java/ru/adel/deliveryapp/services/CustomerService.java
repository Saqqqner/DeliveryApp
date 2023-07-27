package ru.adel.deliveryapp.services;

import ru.adel.deliveryapp.models.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    Customer updateCustomer(Long id, Customer customer);

    void deleteCustomer(Long id);

    Optional<Customer> getCustomerByEmail(String email);
}
