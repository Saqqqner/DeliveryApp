package ru.adel.deliveryapp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.adel.deliveryapp.dto.AuthenticationDTO;
import ru.adel.deliveryapp.dto.CustomerDTO;
import ru.adel.deliveryapp.models.Customer;
import ru.adel.deliveryapp.security.JWTUtil;
import ru.adel.deliveryapp.services.impl.RegistrationServiceImpl;
import ru.adel.deliveryapp.util.valid.AuthValidator;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

import static ru.adel.deliveryapp.util.ErrorsUtil.returnErrorsToClient;


@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final RegistrationServiceImpl registrationService;
    private final AuthValidator authValidator;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;


    public RegistrationController(RegistrationServiceImpl registrationService, AuthValidator authValidator, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.registrationService = registrationService;
        this.authValidator = authValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid CustomerDTO customerDTO,
                                                   BindingResult bindingResult) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        authValidator.validate(customer, bindingResult);
        if (bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }

        registrationService.registerCustomer(customer);

        String token = jwtUtil.generateToken(customer.getEmail());

        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(authInputToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect credentials");
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return Collections.singletonMap("jwt-token", token);
    }


}
