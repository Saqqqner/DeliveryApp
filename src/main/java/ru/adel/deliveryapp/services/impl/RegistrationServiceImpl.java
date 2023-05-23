package ru.adel.deliveryapp.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.adel.deliveryapp.models.Customer;
import ru.adel.deliveryapp.models.CustomerRole;
import ru.adel.deliveryapp.repositories.CustomerRepository;
import ru.adel.deliveryapp.services.RegistrationService;
import ru.adel.deliveryapp.util.MyException;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public RegistrationServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public void registerCustomer(Customer customer) {
        // Проверка на уникальность email
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new MyException("Email is already taken");
        }

        // Создание нового объекта Customer
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(CustomerRole.ROLE_USER);

        // Сохранение в репозитории
        customerRepository.save(customer);
    }
}

