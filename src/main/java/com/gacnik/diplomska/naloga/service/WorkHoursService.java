package com.gacnik.diplomska.naloga.service;

import com.gacnik.diplomska.naloga.controller.EmployeeController;
import com.gacnik.diplomska.naloga.exceptions.EmployeeNotFoundException;
import com.gacnik.diplomska.naloga.model.MonthlyWorkHours;
import com.gacnik.diplomska.naloga.model.enums.WorkHourType;
import com.gacnik.diplomska.naloga.model.WorkHours;
import com.gacnik.diplomska.naloga.model.WorkhourLog;
import com.gacnik.diplomska.naloga.repo.WorkHoursRepository;
import com.gacnik.diplomska.naloga.util.shared.Constants;
import com.gacnik.diplomska.naloga.util.shared.WorkHoursUtils;
import kotlin.time.Duration;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class WorkHoursService {

    private final Logger log = LoggerFactory.getLogger(WorkHoursService.class);
    private final WorkHoursRepository workHoursRepository;

    public String addNewEntry(String uuid, WorkHourType workHourType){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        log.warn(uuid+"_"+ calendar.get(Calendar.MONTH) +"_"+ calendar.get(Calendar.YEAR));
        Optional<MonthlyWorkHours> monthlyWorkHoursOptional = workHoursRepository.findById(uuid+"_"+ calendar.get(Calendar.MONTH) +"_"+ calendar.get(Calendar.YEAR));
        if(monthlyWorkHoursOptional.isPresent()) {
            MonthlyWorkHours monthlyWorkHours = monthlyWorkHoursOptional.get();
            Map<Integer, WorkHours> workHours = monthlyWorkHours.getWorkHours();
            workHours.put(calendar.get(Calendar.DATE), new WorkHours(LocalDateTime.now(), workHourType));
            monthlyWorkHours.setWorkHours(workHours);
            return workHoursRepository.save(monthlyWorkHours).toString();
        } else {
            MonthlyWorkHours monthlyWorkHours = new MonthlyWorkHours();
            monthlyWorkHours.setUuid(uuid+"_"+ calendar.get(Calendar.MONTH) +"_"+ calendar.get(Calendar.YEAR));
            Map<Integer, WorkHours> workHours = new HashMap<>();
            workHours.put(calendar.get(Calendar.DATE), new WorkHours(LocalDateTime.now(), workHourType));
            monthlyWorkHours.setWorkHours(workHours);
            return workHoursRepository.save(monthlyWorkHours).toString();
        }
    }

    public MonthlyWorkHours endEntry(String uuid) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Optional<MonthlyWorkHours> monthlyWorkHoursOptional = workHoursRepository.findById(uuid+"_"+ calendar.get(Calendar.MONTH) +"_"+ calendar.get(Calendar.YEAR));
        if(monthlyWorkHoursOptional.isEmpty()) {
            throw new EmployeeNotFoundException("No start entry found for this employee");
        }
        MonthlyWorkHours monthlyWorkHours = monthlyWorkHoursOptional.get();
        Map<Integer, WorkHours> workHours = monthlyWorkHours.getWorkHours();
        if(workHours.get(calendar.get(Calendar.DATE)) == null) {
            throw new Exception("Work hours not started for today"); // create new expetion
        }
        WorkHours today = workHours.get(calendar.get(Calendar.DATE));
        today.setEndTime(LocalDateTime.now());
        today.setTotalTime(WorkHoursUtils.calculateWorkTime(today.getStartTime(), today.getEndTime()));
        workHours.put(Calendar.DATE,today);
        monthlyWorkHours.setWorkHours(workHours);
        return workHoursRepository.save(monthlyWorkHours);
    }
//
//    // can be used for sick leave and vacation
//    public void addLeaveAsDuration(List<WorkHours> durationOfLeave) {
//        workHoursRepository.insert(durationOfLeave);
//    }
//
//    public void addLeaveToday(String uuid, WorkHourType type) {
//        workHoursRepository.insert(new WorkHours(uuid, LocalDateTime.now(), LocalDateTime.now(), type, Constants.NORMAL_WORK_TIME));
//    }
//
//    public List<WorkHours> addTodaysLogs(WorkhourLog[] hours){
//        List<WorkHours> employeeList = new ArrayList<WorkHours>();
//        for (WorkhourLog workHour: hours
//             ) {
//            employeeList.add(workHoursRepository.insert(
//                    new WorkHours(
//                            workHour.getEmployeeUuid(),
//                            workHour.getStartTime(),
//                            workHour.getLastPing(),
//                            WorkHourType.WORK,
//                            WorkHoursUtils.calculateWorkTime(workHour.getStartTime(), workHour.getLastPing())
//                        )
//                    )
//            );
//
//
//        }
//        return employeeList;
//    }
}
