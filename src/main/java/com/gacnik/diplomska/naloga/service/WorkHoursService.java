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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalAmount;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

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
    // returns map of hours in long for the current week
    public Map<Integer, Long> getWorkhoursOfCurrentWeek(String employeeUuid) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = 1;
        Map<Integer, Long> hours = new HashMap<>();
        LocalDate monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        log.warn(monday + "/"+sunday);
        if(monday.getMonth() != sunday.getMonth()) {
           MonthlyWorkHours monthlyWorkHoursPrev = workHoursRepository.findById(employeeUuid+"_"+ monday.getMonth() +"_"+ monday.getYear()).get();
           Optional<MonthlyWorkHours> monthlyWorkHoursNextOpt = workHoursRepository.findById(employeeUuid+"_"+ sunday.getMonth() +"_"+ sunday.getYear());
            for(int i = monday.getDayOfMonth();monthlyWorkHoursPrev.getWorkHours().get(i) != null; i++) {
                hours.put(day, monthlyWorkHoursPrev.getWorkHours().get(i).getTotalTime());
                day++;
            }
            if(monthlyWorkHoursNextOpt.isPresent()){
                MonthlyWorkHours monthlyWorkHours = monthlyWorkHoursNextOpt.get();
                for(int i = 1; i <= sunday.getDayOfMonth(); i++) {
                    hours.put(day, monthlyWorkHours.getWorkHours().get(i).getTotalTime());
                    day++;
                }
            }
            return hours;
        }
        MonthlyWorkHours monthlyWorkHours = workHoursRepository.findById(employeeUuid+"_"+ calendar.get(Calendar.MONTH) +"_"+ calendar.get(Calendar.YEAR)).get();
        for(int i = monday.getDayOfMonth();monthlyWorkHours.getWorkHours().get(i) != null; i++) {
            hours.put(day, monthlyWorkHours.getWorkHours().get(i).getTotalTime());
            day++;
        }
        return hours;
    }

    // generates a month of workhours with random daily time between 7 & 10 hours
    public boolean createTestData(String employeeId, int month, int year) {
        MonthlyWorkHours monthlyWorkHours = new MonthlyWorkHours();
        monthlyWorkHours.setUuid(employeeId+"_"+ month +"_"+ year);
        Map<Integer, WorkHours> workHours = new HashMap<>();
        for (int i = 1; i < 30; i++ ){
            double generatedDouble = 7.0 + (double) (Math.random() * (10.0 - 7.0));
            LocalDateTime now;
            LocalDateTime later;
            now = LocalDateTime.now();
            later = LocalDateTime.now().plusMinutes((long)(generatedDouble*60));
            workHours.put(i, new WorkHours(now, later, WorkHourType.WORK,WorkHoursUtils.calculateWorkTime(now, later)));
            if(i%5 == 0) {
                i+=2;
            }
        }
        monthlyWorkHours.setWorkHours(workHours);
        workHoursRepository.save(monthlyWorkHours);
        return true;
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
