package ru.adel.deliveryapp.util;


public class ErrorMessage {
    private String message;
    private String fieldName;

    public ErrorMessage(String fieldName , String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

