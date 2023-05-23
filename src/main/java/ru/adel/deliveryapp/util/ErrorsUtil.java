package ru.adel.deliveryapp.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ErrorsUtil {

    public static ResponseEntity<List<ErrorMessage>> returnErrorsToClient(BindingResult bindingResult) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            errorMessages.add(new ErrorMessage(fieldName, errorMessage));
        }
        return ResponseEntity.badRequest().body(errorMessages);
    }

}
