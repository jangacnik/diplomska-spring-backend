package com.gacnik.diplomska.naloga.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/employees")
@AllArgsConstructor
@Log4j2
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping("/all")
    public ResponseEntity<List<Employee>> fetchAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Employee>>fetchEmployeeByName(@PathVariable String name){
        return new ResponseEntity<>(employeeService.getEmployeesByName(name), HttpStatus.FOUND);
    }
    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<Employee>> fetchEmployeeBySurname(@PathVariable String surname){
        return new ResponseEntity<>(employeeService.getEmployeesBySurname(surname), HttpStatus.FOUND);
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<Employee> fetchEmployeeByUuid(@PathVariable String uuid){
        return new ResponseEntity<>(employeeService.getEmployeeById(uuid),  HttpStatus.FOUND);
    }

    @PostMapping(value = "/new",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addNewEmployee(@RequestBody Employee employee) {

        return new ResponseEntity<Employee>(employeeService.addNewEmployee(employee), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Employee> updateEmployeeInfo(@RequestBody Employee changes) {
            return new ResponseEntity<>(
                    employeeService.updateEmployeeData(changes), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String uuid){
        return new ResponseEntity<>(employeeService.deleteEmployee(uuid), HttpStatus.ACCEPTED);
    }
}
