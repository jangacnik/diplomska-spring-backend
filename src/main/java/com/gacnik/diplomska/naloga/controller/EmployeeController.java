package com.gacnik.diplomska.naloga.controller;

import com.gacnik.diplomska.naloga.model.Device;
import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.service.EmployeeService;
import com.gacnik.diplomska.naloga.util.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private final JwtTokenUtil jwtTokenUtil;

    //pridobi vse delavce
    @GetMapping("/all")
    public ResponseEntity<List<Employee>> fetchAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    // vrni podatke o trenutnem uporabniku preko JWT zetona
    @GetMapping("/user")
    public ResponseEntity<Employee> fetchUserData(@RequestHeader (name="Authorization") String token) {
        return new ResponseEntity<>(employeeService.getEmployeeByEmail(jwtTokenUtil.getUsernameFromToken(token.substring(7))), HttpStatus.OK);
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
    public ResponseEntity<Void> addNewEmployee(@RequestBody Employee employee) {
        employeeService.addNewEmployee(employee);
        log.info("New user was created with name: {} {}", employee.getName(), employee.getSurname());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // posodabljenje delavcev
    @PutMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Employee> updateEmployeeInfo(@RequestBody HashMap<String, String> changes) {
        log.info("Employee info of user with ID: '{}' was updated. Changes: {}",changes.get("id"),changes);
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
    public ResponseEntity<Void> addNewDevice(@PathVariable String uuid, @RequestBody Device deviceId){
        employeeService.addDevice(uuid, deviceId);
        log.info("Device: {} was added for user with ID: '{}'", deviceId, uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //odstranjevanje naprav
    @DeleteMapping(value = "/delete/device/{uuid}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String uuid, @RequestBody Device deviceId) {
        employeeService.deleteDevice(uuid, deviceId);
        log.info("Device: {} was removed for user with ID: '{}'", deviceId, uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/device/{uuid}")
    public ResponseEntity<List<Device>> getAllDevicesOfEmployee(@PathVariable String uuid){
        return new ResponseEntity<>(employeeService.getAllDevicesByEmployee(uuid),HttpStatus.OK);
    }

    @GetMapping(value = "/device/id/{deviceId}")
    public ResponseEntity<Employee> getEmployeeByDeviceId(@PathVariable String deviceId) {
        return new ResponseEntity<>(employeeService.findEmployeeByDeviceId(deviceId),HttpStatus.OK);

    }

    @DeleteMapping(value = "/delete/devices/{uuid}")
    public ResponseEntity<Void> deleteAllDevicesOfEmployee(@PathVariable String uuid){
        employeeService.deleteAllDevicesOfEmployee(uuid);
        log.info("All devices were removed for user with ID: '{}'", uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
