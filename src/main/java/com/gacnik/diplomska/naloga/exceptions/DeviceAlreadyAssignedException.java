package com.gacnik.diplomska.naloga.exceptions;

public class DeviceAlreadyAssignedException extends RuntimeException{
    public DeviceAlreadyAssignedException(String msg) {
        super("Device with id " + msg + " is already assigned");
    }
}
