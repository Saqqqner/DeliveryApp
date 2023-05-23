package ru.adel.deliveryapp.util.valid;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.adel.deliveryapp.dto.ProductDTO;

import java.math.BigDecimal;

@Component
public class ProductDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ProductDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductDTO productDTO = (ProductDTO) target;
        if (productDTO.getName() != null && productDTO.getName().length() > 50) {
            errors.rejectValue("name", "length", "Name must be less than or equal to 50 characters");
        }
        if (productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("price", "positive", "Price must be greater than zero");
        }
    }
}



