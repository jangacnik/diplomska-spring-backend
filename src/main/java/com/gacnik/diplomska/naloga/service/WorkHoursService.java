package com.gacnik.diplomska.naloga.service;

import com.gacnik.diplomska.naloga.exceptions.EmployeeNotFoundException;
import com.gacnik.diplomska.naloga.model.DateDuration;
import com.gacnik.diplomska.naloga.model.WorkHourType;
import com.gacnik.diplomska.naloga.model.WorkHours;
import com.gacnik.diplomska.naloga.repo.WorkHoursRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
        long timeDiff = ChronoUnit.MINUTES.between(workHours.getStartTime(), workHours.getEndTime());
                //Math.abs(workHours.getEndTime().get()-workHours.getStartTime().getTime());
        workHours.setTotalTime(timeDiff);
        return workHoursRepository.save(workHours);
    }

    public void addSickLeaveAsDuration(List<WorkHours> durationOfLeave) {
        workHoursRepository.insert(durationOfLeave);
    }

    public void addLeave(DateDuration dateDuration) {

    }
}
