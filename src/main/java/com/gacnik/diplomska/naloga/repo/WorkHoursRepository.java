package com.gacnik.diplomska.naloga.repo;

import com.gacnik.diplomska.naloga.model.WorkHours;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkHoursRepository extends MongoRepository<WorkHours, String> {
}
