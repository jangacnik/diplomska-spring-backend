package com.gacnik.diplomska.naloga.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gacnik.diplomska.naloga.model.enums.WorkHourType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Document(collection = "workHours")
@AllArgsConstructor
@NoArgsConstructor
public class WorkHours {

    @Id
    private String uuid; // id is day of the month
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    private WorkHourType workHourType;
    private ArrayList<WorkHoursBreaks> breaks;
    private Long breakTime;
    private Long totalTime; // in minutes for ease of calculations

    public WorkHours( LocalDateTime startTime, WorkHourType workHourType) {
        this.startTime = startTime;
        this.workHourType = workHourType;
    }
    // for sick leave and holiday leave
    public WorkHours( WorkHourType workHourType, long totalTime) {
        this.workHourType = workHourType;
        this.totalTime = totalTime;
    }

    public WorkHours(String uuid, LocalDateTime startTime, WorkHourType workHourType, long totalTime) {
        this.uuid = uuid;
        this.startTime = startTime;
        this.workHourType = workHourType;
        this.totalTime = totalTime;
    }

    public WorkHours( LocalDateTime startTime, LocalDateTime endTime, WorkHourType workHourType, long totalTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.workHourType = workHourType;
        this.totalTime = totalTime;
    }
    public WorkHours( LocalDateTime startTime, LocalDateTime endTime, WorkHourType workHourType, long totalTime, ArrayList<WorkHoursBreaks> workHoursBreaks) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.workHourType = workHourType;
        this.totalTime = totalTime;
        this.breaks = workHoursBreaks;
    }



    public WorkHours(LocalDateTime startTime, LocalDateTime endTime, WorkHourType workHourType, ArrayList<WorkHoursBreaks> breaks, Long breakTime, Long totalTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.workHourType = workHourType;
        this.breaks = breaks;
        this.breakTime = breakTime;
        this.totalTime = totalTime;
    }
}
