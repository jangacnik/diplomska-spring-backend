package com.gacnik.diplomska.naloga.service;

import com.gacnik.diplomska.naloga.exceptions.DeviceAlreadyAssignedException;
import com.gacnik.diplomska.naloga.exceptions.DeviceNotFoundException;
import com.gacnik.diplomska.naloga.exceptions.EmployeeNotCreatedException;
import com.gacnik.diplomska.naloga.exceptions.EmployeeNotFoundException;
import com.gacnik.diplomska.naloga.model.Address;
import com.gacnik.diplomska.naloga.model.Device;
import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.repo.EmployeeRepository;
import com.gacnik.diplomska.naloga.util.shared.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final Validator validator;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, Validator validator, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("id: " + id)
        );
    }

    public List<Employee> getEmployeesByName(String name) {
        if (employeeRepository.findEmployeesByNameContaining(name).isEmpty()) {
            throw new EmployeeNotFoundException("name: " + name);
        }
        return employeeRepository.findEmployeesByNameContaining(name);
    }

    public List<Employee> getEmployeesBySurname(String surname) {
        if (employeeRepository.findEmployeesBySurnameContaining(surname).isEmpty()) {
            throw new EmployeeNotFoundException(surname);
        }
        return employeeRepository.findEmployeesBySurnameContaining("surname: " + surname);
    }

    public Employee getEmployeeByEmailAuth(String email) {
        if (employeeRepository.findEmployeeByEmail(email) != null) {
            return employeeRepository.findEmployeeByEmail(email);
        }
        throw new EmployeeNotFoundException("e-mail: " + email);
    }

    public Employee getEmployeeByEmail(String email) {
        if (employeeRepository.findEmployeeByEmail(email) != null) {
            Employee returnEmployee = employeeRepository.findEmployeeByEmail(email);
            returnEmployee.setPassword(null);
//            returnEmployee.setRoles(null);
            return returnEmployee;
        }
        throw new EmployeeNotFoundException("e-mail: " + email);
    }


    public void addNewEmployee(Employee employee) {
        Set<ConstraintViolation<Employee>> violation = validator.validate(employee);
        if (!violation.isEmpty())
            throw new ConstraintViolationException(violation);
        if (
                employeeRepository.findFirstEmployeeByEmailOrPhone(employee.getEmail(), employee.getPhone()).isEmpty() && (employee.getDeviceId() == null ||
                        employeeRepository.findEmployeeByDeviceIdMac(employee.getDeviceId().get(0).getMac()).isEmpty())) {
            if (employee.getPassword() == null) {
                employee.setPassword(passwordEncoder.encode(employee.getName() + employee.getSurname()));
            } else {
                employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            }
            if(employee.getRoles() == null) {
                ArrayList<SimpleGrantedAuthority> roles = new ArrayList<>();
                roles.add(new SimpleGrantedAuthority(Roles.USER.toString()));
                employee.setRoles(roles);
            }
            employeeRepository.insert(employee);
            return;
        }
        throw new EmployeeNotCreatedException(" employee with E-mail, Phone number or Device ID already exists");
    }

    public String deleteEmployee(String id) {
        if (employeeRepository.findById(id).isEmpty()) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
        return "Employee with " + id + " successfully deleted";
    }

    public Employee updateEmployeeData(HashMap<String,String> changes) {
        Optional<Employee> employeeOptional = employeeRepository.findById(changes.get("id"));
        if (employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("uuid: " + changes.get("id"));
        }
        Employee employee = employeeOptional.get();
        employee.setSurname(changes.get("surname"));
        employee.setName(changes.get("name"));
        Address address = employee.getAddress();
        address.setCity(changes.get("city"));
        address.setStreet(changes.get("street"));
        address.setPostalCode(changes.get("postalCode"));
        employee.setAddress(address);
        return employeeRepository.save(employee);
    }

    public void addDevice(String id, Device deviceId) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("id: " + id));
        if (!employee.getDeviceId().contains(deviceId)) {
            employee.getDeviceId().add(deviceId);
            employeeRepository.save(employee);
        }
        throw new DeviceAlreadyAssignedException(deviceId.getMac());
    }

    public void deleteDevice(String id, Device deviceId) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("id: " + id));
        if (employee.getDeviceId().contains(deviceId)) {
            employee.getDeviceId().add(deviceId);
            employeeRepository.save(employee);
        }
        throw new DeviceNotFoundException(deviceId.getMac());
    }

    public List<Device> getAllDevicesByEmployee(String id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("id: " + id));
        return employee.getDeviceId();
    }

    public void deleteAllDevicesOfEmployee(String id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("id: " + id));
        employee.setDeviceId(new ArrayList<Device>());
        employeeRepository.save(employee);
    }

    public Employee findEmployeeByDeviceId(String deviceId) {
        return employeeRepository.findEmployeeByDeviceIdContaining(deviceId);
    }
}
