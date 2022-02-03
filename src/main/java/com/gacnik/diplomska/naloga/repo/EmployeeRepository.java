package com.gacnik.diplomska.naloga.repo;

import com.gacnik.diplomska.naloga.model.Device;
import com.gacnik.diplomska.naloga.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee>findFirstEmployeeByEmailOrPhone(String email, String phone);
    Employee findEmployeeByEmail(String email);

    List<Employee>findEmployeesByNameContaining(String name);
    List<Employee>findEmployeesBySurnameContaining(String surname);
    Employee findEmployeeByDeviceIdContaining(String id);

    @Query(value = "{ 'deviceId.mac' : ?0 }", fields = "{ 'deviceId.mac' : 0 }")
    List<Employee> findEmployeeByDeviceIdMac(String devices);



}
