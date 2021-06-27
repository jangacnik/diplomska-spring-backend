package com.gacnik.diplomska.naloga.util;

import lombok.experimental.UtilityClass;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@UtilityClass
public class MonthlyTasks {

    @Scheduled(cron = "0 59 23 1 1/1 ? *") // every 1st of month at midnight
    public void calculateMonthlyHours() {

    }
}
