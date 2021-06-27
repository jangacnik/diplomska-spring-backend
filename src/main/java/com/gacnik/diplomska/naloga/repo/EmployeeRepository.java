package com.gacnik.diplomska.naloga.repo;

import com.gacnik.diplomska.naloga.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface EmployeeRepository extends MongoRepository<Employee, String> {

}
