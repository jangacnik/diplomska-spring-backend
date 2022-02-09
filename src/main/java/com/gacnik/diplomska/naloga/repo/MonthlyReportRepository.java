package com.gacnik.diplomska.naloga.repo;

import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.model.MonthlyHours;
import com.gacnik.diplomska.naloga.model.MonthlyReport;
import com.gacnik.diplomska.naloga.model.WorkHours;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface MonthlyReportRepository extends MongoRepository<MonthlyReport, String> {
    ArrayList<MonthlyReport> findAllByUuidContaining(String substr);
}
