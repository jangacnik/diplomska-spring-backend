package com.gacnik.diplomska.naloga.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class WorkhourLog {
    @Id
    private String uuid;
    private String employeeUuid;
    private LocalDateTime startTime;
    private LocalDateTime lastPing;
}
