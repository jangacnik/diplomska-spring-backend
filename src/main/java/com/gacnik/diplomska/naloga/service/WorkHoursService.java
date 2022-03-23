package com.gacnik.diplomska.naloga.service;

import com.gacnik.diplomska.naloga.exceptions.EmployeeNotFoundException;
import com.gacnik.diplomska.naloga.model.MonthlyWorkHours;
import com.gacnik.diplomska.naloga.model.enums.WorkHourType;
import com.gacnik.diplomska.naloga.model.WorkHours;
import com.gacnik.diplomska.naloga.repo.WorkHoursRepository;
import com.gacnik.diplomska.naloga.util.shared.WorkHoursUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkHoursService {

    private final Logger log = LoggerFactory.getLogger(WorkHoursService.class);
    private final WorkHoursRepository workHoursRepository;


    public String addNewEntry(String uuid, WorkHourType workHourType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        log.warn(uuid + "_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR));
        Optional<MonthlyWorkHours> monthlyWorkHoursOptional = workHoursRepository.findById(uuid + "_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR));
        if (monthlyWorkHoursOptional.isPresent()) {
            MonthlyWorkHours monthlyWorkHours = monthlyWorkHoursOptional.get();
            Map<Integer, WorkHours> workHours = monthlyWorkHours.getWorkHours();
            workHours.put(calendar.get(Calendar.DATE), new WorkHours(LocalDateTime.now(), workHourType));
            monthlyWorkHours.setWorkHours(workHours);
            return workHoursRepository.save(monthlyWorkHours).toString();
        } else {
            MonthlyWorkHours monthlyWorkHours = new MonthlyWorkHours();
            monthlyWorkHours.setUuid(uuid + "_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR));
            Map<Integer, WorkHours> workHours = new HashMap<>();
            workHours.put(calendar.get(Calendar.DATE), new WorkHours(LocalDateTime.now(), workHourType));
            monthlyWorkHours.setWorkHours(workHours);
            return workHoursRepository.save(monthlyWorkHours).toString();
        }
    }

    public MonthlyWorkHours endEntry(String uuid) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Optional<MonthlyWorkHours> monthlyWorkHoursOptional = workHoursRepository.findById(uuid + "_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR));
        if (monthlyWorkHoursOptional.isEmpty()) {
            throw new EmployeeNotFoundException("No start entry found for this employee");
        }
        MonthlyWorkHours monthlyWorkHours = monthlyWorkHoursOptional.get();
        Map<Integer, WorkHours> workHours = monthlyWorkHours.getWorkHours();
        if (workHours.get(calendar.get(Calendar.DATE)) == null) {
            throw new Exception("Work hours not started for today"); // create new expetion
        }
        WorkHours today = workHours.get(calendar.get(Calendar.DATE));
        today.setEndTime(LocalDateTime.now());
        today.setTotalTime(WorkHoursUtils.calculateWorkTime(today.getStartTime(), today.getEndTime()));
        workHours.put(Calendar.DATE, today);
        monthlyWorkHours.setWorkHours(workHours);
        return workHoursRepository.save(monthlyWorkHours);
    }

    // returns map of hours in long for the current week
    public Map<DayOfWeek, Long> getWorkhoursOfCurrentWeek(String employeeUuid) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        MonthlyWorkHours monthlyWorkHoursPrev;
        MonthlyWorkHours monthlyWorkHours;
        Map<DayOfWeek, Long> hours = new HashMap<>();
        // get LocalDate of current weeks monday and Sunday
        LocalDate monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        log.warn(monday + "/" + sunday);
        // if months differ
        if (monday.getMonth() != sunday.getMonth()) {
            log.warn(employeeUuid + "_" + monday.getMonth().getValue() + "_" + monday.getYear());
            // fetch hours of previous month depending on monday date
            Optional<MonthlyWorkHours> monthlyWorkHoursPrevOpt = workHoursRepository.findById(employeeUuid + "_" + monday.getMonth().getValue() + "_" + monday.getYear());
            // map all dates with dayOfMonth >= monday.dayOfMonth
            if (monthlyWorkHoursPrevOpt.isPresent()) {
                monthlyWorkHoursPrev = monthlyWorkHoursPrevOpt.get();
                Map<Integer, WorkHours> mapPrev = monthlyWorkHoursPrev.getWorkHours().entrySet().stream().filter(x -> x.getKey() >= monday.getDayOfMonth())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                // map key as dayOfWeek, value as totalHours
                mapPrev.forEach((k, v) -> hours.put(LocalDate.of(monday.getYear(), monday.getMonth(), k).getDayOfWeek(), v.getTotalTime()));
            }
            // map all dates with dayOfMonth >= sunday.dayOfMonth
            Optional<MonthlyWorkHours> monthlyWorkHoursNextOpt = workHoursRepository.findById(employeeUuid + "_" + sunday.getMonth().getValue() + "_" + sunday.getYear());
            if (monthlyWorkHoursNextOpt.isPresent()) {
                monthlyWorkHours = monthlyWorkHoursNextOpt.get();
                Map<Integer, WorkHours> mapNext = monthlyWorkHours.getWorkHours().entrySet().stream().filter(x -> x.getKey() <= sunday.getDayOfMonth())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                // map key as dayOfWeek, value as totalHours
                mapNext.forEach((k, v) -> hours.put(LocalDate.of(sunday.getYear(), sunday.getMonth(), k).getDayOfWeek(), v.getTotalTime()));
            }
            return hours;
        }
        // if months dont differ, fetch current month hours and map them to hours
        Optional<MonthlyWorkHours> monthlyWorkHoursOptional = workHoursRepository.findById(employeeUuid + "_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR));
        monthlyWorkHoursOptional.ifPresent(workHours -> workHours.getWorkHours().forEach((k, v) -> hours.put(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), k).getDayOfWeek(), v.getTotalTime())));
        return hours;
    }

    // generates a month of workhours with random daily time between 7 & 10 hours
    public boolean createTestData(String employeeId, int month, int year) {
        MonthlyWorkHours monthlyWorkHours = new MonthlyWorkHours();
        monthlyWorkHours.setUuid(employeeId + "_" + month + "_" + year);
        Map<Integer, WorkHours> workHours = new HashMap<>();
        for (int i = 1; i < 30; i++) {
            double generatedDouble = 7.0 + (double) (Math.random() * (10.0 - 7.0));
            LocalDateTime now;
            LocalDateTime later;
            now = LocalDateTime.now();
            later = LocalDateTime.now().plusMinutes((long) (generatedDouble * 60));
            workHours.put(i, new WorkHours(now, later, WorkHourType.WORK, WorkHoursUtils.calculateWorkTime(now, later)));
            if (i % 5 == 0) {
                i += 2;
            }
        }
        monthlyWorkHours.setWorkHours(workHours);
        workHoursRepository.save(monthlyWorkHours);
        return true;
    }

    public String calculateCurrentWorkTime(String employeeId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        log.warn(employeeId + "_" + (calendar.get(Calendar.MONTH) + 1) + "_" + calendar.get(Calendar.YEAR));
        Optional<MonthlyWorkHours> monthlyWorkHoursOptional = workHoursRepository.findById(employeeId + "_" + (calendar.get(Calendar.MONTH) + 1) + "_" + calendar.get(Calendar.YEAR));
        if (monthlyWorkHoursOptional.isPresent()) {
            WorkHours workHours = monthlyWorkHoursOptional.get().getWorkHours().get(calendar.get(Calendar.DATE));
            if (workHours.getTotalTime() != null) {
                return workHours.getTotalTime().toString();
            }
            Long t = WorkHoursUtils.calculateWorkTime(workHours.getStartTime(), LocalDateTime.now());
            return t.toString();
        } else {
            throw new EmployeeNotFoundException("Not found");
        }
    }


    public Boolean getEmployeeStatus(String id) {

        Map<Integer, WorkHours> hoursPerMonth = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Optional<MonthlyWorkHours> monthlyWorkHoursOptional = workHoursRepository.findById(id + "_" + (calendar.get(Calendar.MONTH) + 1) + "_" + calendar.get(Calendar.YEAR));
        if (monthlyWorkHoursOptional.isEmpty()) {
            return false;
        }
        MonthlyWorkHours monthlyWorkHours = monthlyWorkHoursOptional.get();
        hoursPerMonth = monthlyWorkHours.getWorkHours();
        return hoursPerMonth.get(calendar.get(Calendar.DATE)) != null && hoursPerMonth.get(calendar.get(Calendar.DATE)).getEndTime() == null;
    }

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
