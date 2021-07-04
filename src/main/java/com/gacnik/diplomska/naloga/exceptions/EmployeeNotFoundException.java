package com.gacnik.diplomska.naloga.exceptions;

public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(String msg) {
        super("Could not find employee with " + msg);
    }
}
