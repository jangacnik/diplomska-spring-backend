package com.gacnik.diplomska.naloga.advice;

import com.gacnik.diplomska.naloga.controller.EmployeeController;
import com.gacnik.diplomska.naloga.exceptions.DeviceAlreadyAssignedException;
import com.gacnik.diplomska.naloga.exceptions.DeviceNotFoundException;
import com.gacnik.diplomska.naloga.exceptions.EmployeeNotCreatedException;
import com.gacnik.diplomska.naloga.exceptions.EmployeeNotFoundException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.yaml.snakeyaml.constructor.ConstructorException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice(assignableTypes = {EmployeeController.class})
public class EmployeeAdvice {
    @ResponseBody
    @ExceptionHandler(EmployeeNotCreatedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String employeeNotCreatedHandler(EmployeeNotCreatedException ex){ return ex.getMessage(); }

    @ResponseBody
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(EmployeeNotFoundException ex) {
        return ex.getMessage();
    }


    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String constraintViolationExceptionHandler(ConstraintViolationException ex) { return ex.getMessage();}

    @ResponseBody
    @ExceptionHandler(DeviceAlreadyAssignedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String deviceAlreadyAssignedExceptionHandler(DeviceAlreadyAssignedException ex) {
        return ex.getMessage();
    }
    @ResponseBody
    @ExceptionHandler(DeviceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String deviceNotFoundExceptionHandler(DeviceNotFoundException ex) {
        return ex.getMessage();
    }

}
