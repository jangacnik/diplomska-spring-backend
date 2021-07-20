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

    //pridobi vse delavce
    @GetMapping("/all")
    public ResponseEntity<List<Employee>> fetchAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    // najdi se delavce z imenom
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Employee>>fetchEmployeeByName(@PathVariable String name){
        return new ResponseEntity<>(employeeService.getEmployeesByName(name), HttpStatus.FOUND);
    }

    // najdi vse delavce s priimkom
    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<Employee>> fetchEmployeeBySurname(@PathVariable String surname){
        return new ResponseEntity<>(employeeService.getEmployeesBySurname(surname), HttpStatus.FOUND);
    }

    // najdi delvca po uuid
    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<Employee> fetchEmployeeByUuid(@PathVariable String uuid){
        return new ResponseEntity<>(employeeService.getEmployeeById(uuid),  HttpStatus.FOUND);
    }

    // dodajanje novih delavcev
    @PostMapping(value = "/new",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addNewEmployee(@RequestBody Employee employee) {

        return new ResponseEntity<Employee>(employeeService.addNewEmployee(employee), HttpStatus.CREATED);
    }


    // posodabljenje delavcev
    @PutMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Employee> updateEmployeeInfo(@RequestBody Employee changes) {
            return new ResponseEntity<>(
                    employeeService.updateEmployeeData(changes), HttpStatus.OK);

    }


    // odstranjevanje delavcev
    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String uuid){
        return new ResponseEntity<>(employeeService.deleteEmployee(uuid), HttpStatus.ACCEPTED);
    }


    //dodajanje naprav
    @PostMapping(value = "/add/device/{uuid}")
    public ResponseEntity<String> addNewDevice(@PathVariable String uuid, @RequestBody String deviceId){
        employeeService.addDevice(uuid, deviceId);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
    //odstranjevanje naprav
    @DeleteMapping(value = "/delete/device/{uuid}")
    public ResponseEntity<String> deleteDevice(@PathVariable String uuid, @RequestBody String deviceId) {
        employeeService.deleteDevice(uuid, deviceId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
