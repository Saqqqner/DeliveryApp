package ru.adel.deliveryapp.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.adel.deliveryapp.dto.CustomerDTO;
import ru.adel.deliveryapp.models.Customer;
import ru.adel.deliveryapp.security.CustomerDetails;
import ru.adel.deliveryapp.services.impl.CustomerServiceImpl;
import ru.adel.deliveryapp.util.ErrorsUtil;
import ru.adel.deliveryapp.util.valid.CustomerValidator;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerServiceImpl customerServiceImpl;
    private final ModelMapper modelMapper;
    private final CustomerValidator customerValidator;
    private final PasswordEncoder passwordEncoder;


    @GetMapping()
    public ResponseEntity<List<CustomerDTO>> getCustomers() {
        List<Customer> customers = customerServiceImpl.getAllCustomers();
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CustomerDTO> customerDTOs = customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerDTOs);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long customerId) {
        Customer customer = customerServiceImpl.getCustomerById(customerId);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        return ResponseEntity.ok(customerDTO);
    }


    @PutMapping()
    public ResponseEntity<?> updateCustomer(Authentication authentication, @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        CustomerDetails customerDetails = (CustomerDetails) authentication.getPrincipal();
        Customer authenticatedCustomer = customerDetails.getCustomer();
        Long customerId = authenticatedCustomer.getId();

        Customer existingCustomer = customerServiceImpl.getCustomerById(customerId);

        // Проверка, что пользователь обновляет свои собственные данные
        if (!existingCustomer.getId().equals(customerId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Customer updatedCustomer = modelMapper.map(customerDTO, Customer.class);

        // Применять  изменения только к определенным полям
        if (customerDTO.getName() != null) {
            existingCustomer.setName(updatedCustomer.getName());
        }
        if (customerDTO.getEmail() != null) {
            existingCustomer.setEmail(updatedCustomer.getEmail());
        }
        if (customerDTO.getAddress() != null) {
            existingCustomer.setAddress(updatedCustomer.getAddress());
        }
        if (customerDTO.getPassword() != null) {
            existingCustomer.setPassword(passwordEncoder.encode(updatedCustomer.getPassword()));
        }

        // Валидация обновленных данных
        customerValidator.validate(existingCustomer, bindingResult);
        if (bindingResult.hasErrors()) {
            return ErrorsUtil.returnErrorsToClient(bindingResult);
        }

        // Обновите измененного пользователя в базе данных
        Customer savedCustomer = customerServiceImpl.updateCustomer(customerId, existingCustomer);
        CustomerDTO updatedCustomerDTO = modelMapper.map(savedCustomer, CustomerDTO.class);
        return ResponseEntity.ok(updatedCustomerDTO);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteCustomer(Authentication authentication) {
        CustomerDetails customerDetails = (CustomerDetails) authentication.getPrincipal();
        Long customerId = customerDetails.getCustomer().getId();
        customerServiceImpl.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
