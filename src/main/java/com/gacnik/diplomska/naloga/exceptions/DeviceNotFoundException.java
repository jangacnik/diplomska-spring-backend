package com.gacnik.diplomska.naloga.exceptions;

public class DeviceNotFoundException extends RuntimeException{
    public DeviceNotFoundException(String msg) {
        super("Device with id " + msg + " not found");
    }
}
