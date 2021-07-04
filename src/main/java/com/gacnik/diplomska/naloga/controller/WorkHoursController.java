package com.gacnik.diplomska.naloga.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gacnik.diplomska.naloga.model.DateDuration;
import com.gacnik.diplomska.naloga.model.WorkHourType;
import com.gacnik.diplomska.naloga.model.WorkHours;
import com.gacnik.diplomska.naloga.service.WorkHoursService;
import com.gacnik.diplomska.naloga.util.shared.WorkHoursUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/hours")
@AllArgsConstructor
@Log4j2
public class WorkHoursController {

    private final WorkHoursService workHoursService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/new/{uuid}/{type}")
    public String addNewEntry(@PathVariable String uuid, @PathVariable WorkHourType type) {
        return workHoursService.addNewEntry(uuid, type);
    }

    @PutMapping("/end/{uuid}")
    public WorkHours endEntry(@PathVariable String uuid){
        log.error(uuid);
        return workHoursService.endEntry(uuid);
    }

    @PostMapping(value = "/sick/duration/{uuid}")
    public void addSickLeave(@RequestBody DateDuration dateDuration, @PathVariable String uuid) throws JsonProcessingException {
        log.warn(dateDuration);
        //List<WorkHours> listOfWorkHours = WorkHoursUtils.getDatesFromDuration(dateDuration, uuid, WorkHourType.SICK_LEAVE);
        //workHoursService.addLeaveAsDuration(listOfWorkHours);
    }

    @PostMapping(value = "/vacation/duration/{uuid}")
    public void addLeave(@RequestBody String dateDuration, @PathVariable String uuid) throws JsonProcessingException {
        log.warn(dateDuration);
        List<WorkHours> listOfWorkHours = WorkHoursUtils.getDatesFromDuration(dateDuration, uuid, WorkHourType.LEAVE);
        workHoursService.addLeaveAsDuration(listOfWorkHours);
    }

    @PostMapping(value = "/leave/today/{uuid}")
    public boolean addLeaveToday(@PathVariable String uuid){
        workHoursService.addLeaveToday(uuid, WorkHourType.LEAVE);
        return false;
    }

    @PostMapping(value = "/sick/today/{uuid}")
    public boolean addSichLeaveToday(@PathVariable String uuid){
        workHoursService.addLeaveToday(uuid, WorkHourType.SICK_LEAVE);
        return false;
    }
}
