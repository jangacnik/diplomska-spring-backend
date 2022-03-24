package com.gacnik.diplomska.naloga.repo;

import com.gacnik.diplomska.naloga.model.MonthlyWorkHours;
import com.gacnik.diplomska.naloga.model.WorkHours;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Repository
public interface WorkHoursRepository extends MongoRepository<MonthlyWorkHours, String> {
    ArrayList<MonthlyWorkHours> findAllByUuidContaining(String uuid);
}
