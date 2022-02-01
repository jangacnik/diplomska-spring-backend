package com.gacnik.diplomska.naloga.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Map;

@Data
@NoArgsConstructor
public class MonthlyWorkHours {
    @Id
    private String uuid; // employeeId + _ + month + _ + year
    private Map<Integer, WorkHours> workHours;
}
