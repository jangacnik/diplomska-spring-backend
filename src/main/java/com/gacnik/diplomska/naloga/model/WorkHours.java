package com.gacnik.diplomska.naloga.model;

import com.gacnik.diplomska.naloga.util.UuidGenerator;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    private Date startTime;
    private Date endTime;
    private WorkHourType workHourType;
    private long totalTime; // in minutes for ease of calculations

    public WorkHours(String employeeUuid, Date startTime, WorkHourType workHourType) {
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

    public WorkHours(String uuid, String employeeUuid, Date startTime, WorkHourType workHourType, long totalTime) {
        this.uuid = uuid;
        this.employeeUuid = employeeUuid;
        this.startTime = startTime;
        this.workHourType = workHourType;
        this.totalTime = totalTime;
    }
}
