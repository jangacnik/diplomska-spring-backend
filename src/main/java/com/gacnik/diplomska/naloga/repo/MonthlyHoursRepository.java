package com.gacnik.diplomska.naloga.repo;

import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.model.MonthlyHours;
import com.gacnik.diplomska.naloga.model.WorkHours;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MonthlyHoursRepository extends MongoRepository<MonthlyHours, String> {

}
