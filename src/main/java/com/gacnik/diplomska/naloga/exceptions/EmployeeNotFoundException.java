package com.gacnik.diplomska.naloga.exceptions;

public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(String uuid) {
        super("Could not find employee " + uuid);
    }
}
