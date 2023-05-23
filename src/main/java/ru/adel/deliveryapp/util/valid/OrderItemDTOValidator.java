package ru.adel.deliveryapp.util.valid;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.adel.deliveryapp.dto.OrderItemDTO;

import java.math.BigDecimal;

@Component
public class OrderItemDTOValidator implements Validator {

    private static final String QUANTITY_FIELD = "quantity";

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderItemDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, QUANTITY_FIELD, "field.required");
        OrderItemDTO orderItemDTO = (OrderItemDTO) target;
        if (orderItemDTO.getQuantity() != null && orderItemDTO.getQuantity() < 1) {
            errors.rejectValue(QUANTITY_FIELD, "field.invalid");
        }

        }
    }

