package ru.adel.deliveryapp.util.valid;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.adel.deliveryapp.models.Customer;

@Component
public class AuthValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", "Name is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required", "Email is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "field.required", "Address is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required", "Password is required");
        Customer customer = (Customer) target;
        if (customer.getName() != null && customer.getName().length() > 50) {
            errors.rejectValue("name", "field.length", "Name must be less than or equal to 50 characters");
        }
        if (customer.getEmail() != null && customer.getEmail().length() > 255) {
            errors.rejectValue("email", "field.length", "Email must be less than or equal to 255 characters");
        }
        if (customer.getAddress() != null && customer.getAddress().length() > 100) {
            errors.rejectValue("address", "field.length", "Address must be less than or equal to 100 characters");
        }
        if (customer.getPassword() != null && customer.getPassword().length() > 255) {
            errors.rejectValue("password", "field.length", "Password must be less than or equal to 255 characters");
        }
    }
}