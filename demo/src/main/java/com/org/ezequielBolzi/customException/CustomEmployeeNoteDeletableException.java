package com.org.ezequielBolzi.customException;

public class CustomEmployeeNoteDeletableException extends  RuntimeException {
    public CustomEmployeeNoteDeletableException(String message) {
        super(message);
    }
}
