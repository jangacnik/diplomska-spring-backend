package com.gacnik.diplomska.naloga.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gacnik.diplomska.naloga.model.DateDuration;
import com.gacnik.diplomska.naloga.model.WorkHourType;
import com.gacnik.diplomska.naloga.model.WorkHours;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Log4j2
public class WorkHoursUtils {

    private ObjectMapper mapper = new ObjectMapper();

    public List<WorkHours> getDatesFromDuration(String duration, String uuid, WorkHourType workHourType) throws JsonProcessingException {
        mapper.registerModule(new JavaTimeModule());
        List<WorkHours> workHours = new ArrayList<WorkHours>();
        DateDuration durationTmp = mapper.readValue(duration, DateDuration.class);
        for(int i = 0; i < durationTmp.getNumberOfDays(); i++){
            workHours.add(new WorkHours(uuid, durationTmp.getStartDate().plusDays(i), durationTmp.getStartDate().plusDays(i), workHourType, 8));
        }
        //log.warn("Days : " + numberOfDays);
        //log.warn("Days :  " + Duration.between(durationTmp.getStartDate(), durationTmp.getEndDate()).toDays());
        // durationTmp.getStartDate().until(durationTmp.getEndDate(), ChronoUnit.DAYS);
        //log.error("List: "+workHours);
        return workHours;
    }

}
