package com.gacnik.diplomska.naloga.model;

import com.gacnik.diplomska.naloga.util.UuidGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Getter
@Setter
public class MonthlyHours {
    @Id
    private String uuid;
    private String employeeUuid;
    // all in minutes
    private int totalWorkTime;
    private int totalSickLeaveTime;
    private int totalLeaveTime;
    private int totalTime;


    public MonthlyHours(String employeeUuid, int totalWorkTime, int totalSickLeaveTime, int totalLeaveTime, int totalTime) {
        this.uuid = UuidGenerator.generateUuid();
        this.employeeUuid = employeeUuid;
        this.totalWorkTime = totalWorkTime;
        this.totalSickLeaveTime = totalSickLeaveTime;
        this.totalLeaveTime = totalLeaveTime;
        this.totalTime = totalTime;
    }
}
