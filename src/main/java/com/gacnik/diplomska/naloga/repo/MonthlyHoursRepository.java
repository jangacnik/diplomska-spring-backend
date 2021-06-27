package com.gacnik.diplomska.naloga.repo;

import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.model.MonthlyHours;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MonthlyHoursRepository extends MongoRepository<MonthlyHours, String> {
}
