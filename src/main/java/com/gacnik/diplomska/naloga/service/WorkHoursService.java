package com.gacnik.diplomska.naloga.service;

import com.gacnik.diplomska.naloga.exceptions.EmployeeNotFoundException;
import com.gacnik.diplomska.naloga.model.DateDuration;
import com.gacnik.diplomska.naloga.model.WorkHourType;
import com.gacnik.diplomska.naloga.model.WorkHours;
import com.gacnik.diplomska.naloga.repo.WorkHoursRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Log4j2
public class WorkHoursService {

    private final WorkHoursRepository workHoursRepository;

    public String addNewEntry(String uuid, WorkHourType workHourType){
        return workHoursRepository.insert(new WorkHours(uuid, new Date(), workHourType)).toString();
    }

    public WorkHours endEntry(String uuid) {
        WorkHours workHours = workHoursRepository.findById(uuid).orElseThrow(() -> new EmployeeNotFoundException(uuid)
        );
        workHours.setEndTime(new Date());
        long timeDiff = Math.abs(workHours.getEndTime().getTime()-workHours.getStartTime().getTime());
        workHours.setTotalTime(TimeUnit.MINUTES.convert(timeDiff, TimeUnit.MILLISECONDS));
        return workHoursRepository.save(workHours);
    }

    public void addSickLeave(DateDuration dateDuration) {
        log.error(dateDuration);
    }

    public void addLeave(DateDuration dateDuration) {

    }
}
