package com.gacnik.diplomska.naloga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "monthlyReport")
public class MonthlyReport {
    @Id
    private String uuid; // month + _ + year
    private Map<String, MonthlyHours> monthlyHoursMap;
}
