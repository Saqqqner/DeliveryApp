package ru.adel.deliveryapp.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.adel.deliveryapp.models.Customer;
import ru.adel.deliveryapp.repositories.CustomerRepository;
import ru.adel.deliveryapp.security.CustomerDetails;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (!customer.isPresent()) throw new UsernameNotFoundException("Customer not found with email: " + email);

        return new CustomerDetails(customer.get());
    }


}
