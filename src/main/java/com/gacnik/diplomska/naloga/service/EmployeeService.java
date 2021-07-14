package com.gacnik.diplomska.naloga.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gacnik.diplomska.naloga.exceptions.EmployeeNotCreatedException;
import com.gacnik.diplomska.naloga.exceptions.EmployeeNotFoundException;
import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.repo.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Log4j2
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final Validator validator;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(String uuid) {
        return employeeRepository.findById(uuid).orElseThrow(() -> new EmployeeNotFoundException("uuid: "+uuid)
        );
    }

    public List<Employee> getEmployeesByName(String name) {
        if(employeeRepository.findEmployeesByNameContaining(name).isEmpty()){
            throw new EmployeeNotFoundException("name: "+name);
        }
            return employeeRepository.findEmployeesByNameContaining(name);
    }

    public List<Employee> getEmployeesBySurname(String surname) {
        if(employeeRepository.findEmployeesBySurnameContaining(surname).isEmpty()){
            throw new EmployeeNotFoundException(surname);
        }
            return employeeRepository.findEmployeesBySurnameContaining("surname: "+surname);
    }


    public Employee addNewEmployee(Employee employee){
        Set<ConstraintViolation<Employee>> violation = validator.validate(employee);
        if(!violation.isEmpty())
            throw new ConstraintViolationException(violation);
        if(
                employeeRepository.findFirstEmployeeByEmailOrPhone(employee.getEmail(), employee.getPhone()).isEmpty() && employeeRepository.findEmployeeByDeviceIdContaining(employee.getDeviceId()).isEmpty()){
            employeeRepository.insert(employee);
            return employee;
        }
            throw new EmployeeNotCreatedException(" employee with E-mail, Phone number or Device ID already exists");
    }

    public String deleteEmployee(String uuid) {
        if(employeeRepository.findById(uuid).isEmpty()) {
            throw new EmployeeNotFoundException(uuid);
        }
            employeeRepository.deleteById(uuid);
            return "Employee with " +uuid+" successfully deleted";
    }

    public Employee updateEmployeeData(Employee changes) {
        if(employeeRepository.findById(changes.getUuid()).isEmpty()){
            throw new EmployeeNotFoundException("uuid: "+changes.getUuid());
        }
        return employeeRepository.save(changes);
    }
}
