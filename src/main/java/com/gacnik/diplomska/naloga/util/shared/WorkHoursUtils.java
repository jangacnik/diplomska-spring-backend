package com.gacnik.diplomska.naloga.util.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gacnik.diplomska.naloga.model.DateDuration;
import com.gacnik.diplomska.naloga.model.WorkHoursBreaks;
import com.gacnik.diplomska.naloga.model.enums.WorkHourType;
import com.gacnik.diplomska.naloga.model.WorkHours;
import lombok.experimental.UtilityClass;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class WorkHoursUtils {

    private ObjectMapper mapper = new ObjectMapper();

//    public List<WorkHours> getDatesFromDuration(String duration, String uuid, WorkHourType workHourType) throws JsonProcessingException {
//        mapper.registerModule(new JavaTimeModule());
//        List<WorkHours> workHours = new ArrayList<WorkHours>();
//        DateDuration durationTmp = mapper.readValue(duration, DateDuration.class);
//        for(int i = 0; i < durationTmp.getNumberOfDays(); i++){
//            workHours.add(new WorkHours(uuid, durationTmp.getStartDate().plusDays(i), durationTmp.getStartDate().plusDays(i), workHourType, 8L));
//        }
//        return workHours;
//    }

    public long calculateWorkTime(LocalDateTime start, LocalDateTime end){
        return ChronoUnit.MINUTES.between(start, end);
    }

    public long calculateTotalBreakTime(ArrayList<WorkHoursBreaks> workHoursBreaks) {
        long breakTime = 0;
        for (WorkHoursBreaks breaks: workHoursBreaks) {
            breakTime += ChronoUnit.MINUTES.between(breaks.getStartTime(), breaks.getEndTime());
        }
        return breakTime;
    }

}
