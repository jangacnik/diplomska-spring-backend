package com.gacnik.diplomska.naloga.service;

import com.gacnik.diplomska.naloga.exceptions.DeviceAlreadyAssignedException;
import com.gacnik.diplomska.naloga.exceptions.DeviceNotFoundException;
import com.gacnik.diplomska.naloga.exceptions.EmployeeNotCreatedException;
import com.gacnik.diplomska.naloga.exceptions.EmployeeNotFoundException;
import com.gacnik.diplomska.naloga.model.Device;
import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.repo.EmployeeRepository;
import com.gacnik.diplomska.naloga.util.shared.Roles;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final Validator validator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(String uuid) {
        return employeeRepository.findById(uuid).orElseThrow(() -> new EmployeeNotFoundException("uuid: " + uuid)
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

    public String deleteEmployee(String uuid) {
        if (employeeRepository.findById(uuid).isEmpty()) {
            throw new EmployeeNotFoundException(uuid);
        }
        employeeRepository.deleteById(uuid);
        return "Employee with " + uuid + " successfully deleted";
    }

    public Employee updateEmployeeData(Employee changes) {
        if (employeeRepository.findById(changes.getUuid()).isEmpty()) {
            throw new EmployeeNotFoundException("uuid: " + changes.getUuid());
        }
        return employeeRepository.save(changes);
    }

    public void addDevice(String uuid, Device deviceId) {
        Employee employee = employeeRepository.findById(uuid).orElseThrow(() -> new EmployeeNotFoundException("uuid: " + uuid));
        if (!employee.getDeviceId().contains(deviceId)) {
            employee.getDeviceId().add(deviceId);
            employeeRepository.save(employee);
        }
        throw new DeviceAlreadyAssignedException(deviceId.getMac());
    }

    public void deleteDevice(String uuid, Device deviceId) {
        Employee employee = employeeRepository.findById(uuid).orElseThrow(() -> new EmployeeNotFoundException("uuid: " + uuid));
        if (employee.getDeviceId().contains(deviceId)) {
            employee.getDeviceId().add(deviceId);
            employeeRepository.save(employee);
        }
        throw new DeviceNotFoundException(deviceId.getMac());
    }

    public List<Device> getAllDevicesByEmployee(String uuid) {
        Employee employee = employeeRepository.findById(uuid).orElseThrow(() -> new EmployeeNotFoundException("uuid: " + uuid));
        return employee.getDeviceId();
    }

    public void deleteAllDevicesOfEmployee(String uuid) {
        Employee employee = employeeRepository.findById(uuid).orElseThrow(() -> new EmployeeNotFoundException("uuid: " + uuid));
        employee.setDeviceId(new ArrayList<Device>());
        employeeRepository.save(employee);
    }

    public Employee findEmployeeByDeviceId(String deviceID) {
        return employeeRepository.findEmployeeByDeviceIdContaining(deviceID);
    }
}
