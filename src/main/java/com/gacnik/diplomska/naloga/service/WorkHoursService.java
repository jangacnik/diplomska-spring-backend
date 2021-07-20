package com.gacnik.diplomska.naloga.service;

import com.gacnik.diplomska.naloga.exceptions.EmployeeNotFoundException;
import com.gacnik.diplomska.naloga.model.enums.WorkHourType;
import com.gacnik.diplomska.naloga.model.WorkHours;
import com.gacnik.diplomska.naloga.model.WorkhourLog;
import com.gacnik.diplomska.naloga.repo.WorkHoursRepository;
import com.gacnik.diplomska.naloga.util.shared.Constants;
import com.gacnik.diplomska.naloga.util.shared.WorkHoursUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class WorkHoursService {

    private final WorkHoursRepository workHoursRepository;

    public String addNewEntry(String uuid, WorkHourType workHourType){
        return workHoursRepository.insert(new WorkHours(uuid, LocalDateTime.now(), workHourType)).toString();
    }

    public WorkHours endEntry(String uuid) {
        WorkHours workHours = workHoursRepository.findById(uuid).orElseThrow(() -> new EmployeeNotFoundException(uuid)
        );
        workHours.setEndTime(LocalDateTime.now());
        workHours.setTotalTime(WorkHoursUtils.calculateWorkTime(workHours.getStartTime(), workHours.getEndTime()));
        return workHoursRepository.save(workHours);
    }

    // can be used for sick leave and vacation
    public void addLeaveAsDuration(List<WorkHours> durationOfLeave) {
        workHoursRepository.insert(durationOfLeave);
    }

    public void addLeaveToday(String uuid, WorkHourType type) {
        workHoursRepository.insert(new WorkHours(uuid, LocalDateTime.now(), LocalDateTime.now(), type, Constants.NORMAL_WORK_TIME));
    }

    public List<WorkHours> addTodaysLogs(WorkhourLog[] hours){
        List<WorkHours> employeeList = new ArrayList<WorkHours>();
        for (WorkhourLog workHour: hours
             ) {
            employeeList.add(workHoursRepository.insert(
                    new WorkHours(
                            workHour.getEmployeeUuid(),
                            workHour.getStartTime(),
                            workHour.getLastPing(),
                            WorkHourType.WORK,
                            WorkHoursUtils.calculateWorkTime(workHour.getStartTime(), workHour.getLastPing())
                        )
                    )
            );


        }
        return employeeList;
    }
}
