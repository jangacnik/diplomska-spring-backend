package com.gacnik.diplomska.naloga.service;

import com.gacnik.diplomska.naloga.model.MonthlyHours;
import com.gacnik.diplomska.naloga.model.MonthlyReport;
import com.gacnik.diplomska.naloga.model.MonthlyWorkHours;
import com.gacnik.diplomska.naloga.model.WorkHours;
import com.gacnik.diplomska.naloga.repo.MonthlyReportRepository;
import com.gacnik.diplomska.naloga.repo.WorkHoursRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MonthlyHoursService {

    private final WorkHoursRepository workHoursRepository;
    private final MonthlyReportRepository monthlyReportRepository;

    public void calculateMonthlyHours() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        MonthlyReport monthlyReport = new MonthlyReport("monthlyReport_"+calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR), new HashMap<>());
        Map<String, MonthlyHours> monthlyHoursMap = new HashMap<>();
        ArrayList<MonthlyWorkHours> monthlyWorkHours = workHoursRepository.findAllByUuidContaining(calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR));
        for (MonthlyWorkHours hours : monthlyWorkHours
        ) {
            String[] employeeUuid = hours.getUuid().split("_");
            MonthlyHours monthlyHours = new MonthlyHours(employeeUuid[0], 0, 0, 0, 0);
            Map<Integer, WorkHours> workHoursMap = hours.getWorkHours();
            long work = 0, sick = 0, leave = 0, total = 0;
            for (Map.Entry<Integer, WorkHours> entry : workHoursMap.entrySet()) {
                switch (entry.getValue().getWorkHourType()) {
                    case WORK -> work += entry.getValue().getTotalTime();
                    case LEAVE -> leave += entry.getValue().getTotalTime();
                    case SICK_LEAVE -> sick += entry.getValue().getTotalTime();
                }
                total += entry.getValue().getTotalTime();
            }
            monthlyHours.setTotalLeaveTime(leave);
            monthlyHours.setTotalSickLeaveTime(sick);
            monthlyHours.setTotalWorkTime(work);
            monthlyHours.setTotalTime(total);
            monthlyHoursMap.put(employeeUuid[0],monthlyHours);
        }
        monthlyReport.setMonthlyHoursMap(monthlyHoursMap);
        monthlyReportRepository.save(monthlyReport);
    }
}
