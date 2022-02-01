package com.gacnik.diplomska.naloga.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyHours {
    @Id
    private String uuid; // employee uuid
    // all in minutes
    private long totalWorkTime;
    private long totalSickLeaveTime;
    private long totalLeaveTime;
    private long totalTime;
}


