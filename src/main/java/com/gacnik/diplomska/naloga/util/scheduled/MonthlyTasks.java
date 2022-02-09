package com.gacnik.diplomska.naloga.util.scheduled;

import com.gacnik.diplomska.naloga.service.MonthlyReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
public class MonthlyTasks {
    @Autowired
    private MonthlyReportService monthlyReportService;

    private final Logger log = LoggerFactory.getLogger(MonthlyTasks.class);
    @Scheduled(cron = "0 0 0 * * ?") // everyday at midnight
    public void calculateMonthlyHours() {
        monthlyReportService.calculateMonthlyHours();
    }
}
