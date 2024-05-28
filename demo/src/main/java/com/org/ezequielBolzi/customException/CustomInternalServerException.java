package com.org.ezequielBolzi.customException;

public class CustomInternalServerException  extends RuntimeException{
    public CustomInternalServerException(String message) {
        super(message);
}
}
