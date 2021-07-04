package com.gacnik.diplomska.naloga.exceptions;

public class EmployeeNotCreatedException extends RuntimeException {
    public EmployeeNotCreatedException(String msg) {
        super("Employee couldn't be created: " + msg);
    }
}
