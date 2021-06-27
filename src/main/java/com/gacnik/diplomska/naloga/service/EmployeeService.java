package com.gacnik.diplomska.naloga.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gacnik.diplomska.naloga.exceptions.EmployeeNotFoundException;
import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.repo.EmployeeRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final Gson gson;

    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(String uuid) {
        return employeeRepository.findById(uuid).orElseThrow(() -> new EmployeeNotFoundException(uuid)
        );
    }

    public List<Employee> getEmployeesByName(String name) {
        if(employeeRepository.findEmployeesByNameContaining(name).isEmpty()){
            throw new EmployeeNotFoundException(name);
        }else {
            return employeeRepository.findEmployeesByNameContaining(name);
        }
    }

    public List<Employee> getEmployeesBySurname(String surname) {
        if(employeeRepository.findEmployeesBySurnameContaining(surname).isEmpty()){
            throw new EmployeeNotFoundException(surname);
        }else {
            return employeeRepository.findEmployeesBySurnameContaining(surname);
        }
    }

    public String addNewEmployee(Employee employee){
        if(employeeRepository.findEmployeeByEmailOrPhone(employee.getEmail(), employee.getPhone()).isEmpty() && employeeRepository.findEmployeeByDeviceIdContaining(employee.getDeviceId()).isEmpty()){
            employeeRepository.insert(employee);
            return "Successfully added new Employee";
        } else {
            return "Couldn't add new Employee - E-mail, Phone or DeviceId already Registered";
        }
    }

    public boolean deleteEmployee(String uuid) {
        if(employeeRepository.findById(uuid).isEmpty()) {
            throw new EmployeeNotFoundException(uuid);
        }else {
            employeeRepository.deleteById(uuid);
            return true;
        }
    }

    public Employee updateEmployeeData(Employee changes) {
        return employeeRepository.save(changes);
    }
}
