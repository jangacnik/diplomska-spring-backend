package com.gacnik.diplomska.naloga.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gacnik.diplomska.naloga.model.enums.WorkHourType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "workHours")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkHours {
    @Id
    private String uuid;
    private String employeeUuid;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    private WorkHourType workHourType;
    private long totalTime; // in minutes for ease of calculations

    public WorkHours(String employeeUuid, LocalDateTime startTime, WorkHourType workHourType) {
        this.employeeUuid = employeeUuid;
        this.startTime = startTime;
        this.workHourType = workHourType;
    }
    // for sick leave and holiday leave
    public WorkHours(String employeeUuid, WorkHourType workHourType, long totalTime) {
        this.employeeUuid = employeeUuid;
        this.workHourType = workHourType;
        this.totalTime = totalTime;
    }

    public WorkHours(String uuid, String employeeUuid, LocalDateTime startTime, WorkHourType workHourType, long totalTime) {
        this.uuid = uuid;
        this.employeeUuid = employeeUuid;
        this.startTime = startTime;
        this.workHourType = workHourType;
        this.totalTime = totalTime;
    }

    public WorkHours(String employeeUuid, LocalDateTime startTime, LocalDateTime endTime, WorkHourType workHourType, long totalTime) {
        this.employeeUuid = employeeUuid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.workHourType = workHourType;
        this.totalTime = totalTime;
    }


}
