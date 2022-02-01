package com.gacnik.diplomska.naloga.repo;

import com.gacnik.diplomska.naloga.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee>findFirstEmployeeByEmailOrPhone(String email, String phone);
    Employee findEmployeeByEmail(String email);
    Optional<Employee>findEmployeeByDeviceIdContaining(List<String> ids);
    List<Employee>findEmployeesByNameContaining(String name);
    List<Employee>findEmployeesBySurnameContaining(String surname);
    Employee findEmployeeByDeviceIdContaining(String id);


}
