package com.gacnik.diplomska.naloga.advice;

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

@ControllerAdvice
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
    @ExceptionHandler(ConstructorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String constructorErrorHandler(ConstructorException ex) { return ex.getMessage();}

    @ResponseBody
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String constructorErrorHandler(IncorrectResultSizeDataAccessException ex) { return ex.getMessage();}

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String constructorErrorHandler(ConstraintViolationException ex) { return ex.getMessage();}


}
