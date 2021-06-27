package com.gacnik.diplomska.naloga.model;

import com.gacnik.diplomska.naloga.util.UuidGenerator;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "workHours")
@Getter
@Setter
public class WorkHours {
    @Id
    private String uuid;
    private String employeeUuid;
    private Date startTime;
    private Date endTime;
    private WorkHourType workHourType;
    private int totalTime; // in minutes for ease of calculations

    public WorkHours(String employeeUuid, Date startTime, WorkHourType workHourType) {
        this.uuid = UuidGenerator.generateUuid();
        this.employeeUuid = employeeUuid;
        this.startTime = startTime;
        this.workHourType = workHourType;
    }
    // for sick leave and holiday leave
    public WorkHours(String employeeUuid, WorkHourType workHourType, int totalTime) {
        this.uuid = UuidGenerator.generateUuid();
        this.employeeUuid = employeeUuid;
        this.workHourType = workHourType;
        this.totalTime = totalTime;
    }
}
