package com.gacnik.diplomska.naloga.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.service.EmployeeService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/employees")
@AllArgsConstructor
@Log4j2
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    private Gson gson;

    @GetMapping("/all")
    public List<Employee> fetchAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/name/{name}")
    public List<Employee> fetchEmployeeByName(@PathVariable String name){
        return employeeService.getEmployeesByName(name);
    }
    @GetMapping("/surname/{surname}")
    public List<Employee> fetchEmployeeBySurname(@PathVariable String surname){
        return employeeService.getEmployeesBySurname(surname);
    }

    @GetMapping("/uuid/{uuid}")
    public Employee fetchEmployeeByUuid(@PathVariable String uuid){
        return employeeService.getEmployeeById(uuid);
    }

    @PostMapping(value = "/new",consumes = "application/json")
    public String addNewEmployee(@RequestBody String employee) {
        try {
            return employeeService.addNewEmployee(gson.fromJson(employee, Employee.class));
        }catch (Exception e) {
            return e.toString();
        }
    }

    @PutMapping(value = "/update", consumes = "application/json")
    public String updateEmployeeInfo(@RequestBody String changes) {
        try {
            return gson.toJson(employeeService.updateEmployeeData(gson.fromJson(changes,Employee.class)));
        }catch (Exception e) {
            return e.toString();
        }
    }

    @DeleteMapping("/delete/{uuid}")
    public boolean deleteEmployeeById(@PathVariable String uuid){
        return employeeService.deleteEmployee(uuid);
    }
}
