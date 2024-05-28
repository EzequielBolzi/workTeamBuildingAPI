package com.org.ezequielBolzi.customException;

public class CustomEmailAlreadyExistsException extends  RuntimeException {
    public CustomEmailAlreadyExistsException(String message) {
        super(message);
    }
}
