package com.gacnik.diplomska.naloga.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gacnik.diplomska.naloga.model.DateDuration;
import com.gacnik.diplomska.naloga.model.WorkHourType;
import com.gacnik.diplomska.naloga.model.WorkHours;
import com.gacnik.diplomska.naloga.model.WorkhourLog;
import com.gacnik.diplomska.naloga.service.WorkHoursService;
import com.gacnik.diplomska.naloga.util.shared.WorkHoursUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/hours")
@AllArgsConstructor
@Log4j2
public class WorkHoursController {

    private final WorkHoursService workHoursService;

    @PostMapping(value = "/new/{uuid}/{type}")
    public ResponseEntity<String> addNewEntry(@PathVariable String uuid, @PathVariable WorkHourType type) {
        return new ResponseEntity<>(workHoursService.addNewEntry(uuid, type), HttpStatus.OK);
    }

    @PutMapping("/end/{uuid}")
    public ResponseEntity<WorkHours> endEntry(@PathVariable String uuid){
        return new ResponseEntity<>(workHoursService.endEntry(uuid), HttpStatus.OK);
    }

    @PostMapping(value = "/sick/duration/{uuid}")
    public ResponseEntity addSickLeave(@RequestBody String dateDuration, @PathVariable String uuid) throws JsonProcessingException {
        log.warn(dateDuration);
        List<WorkHours> listOfWorkHours = WorkHoursUtils.getDatesFromDuration(dateDuration, uuid, WorkHourType.SICK_LEAVE);
       return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/vacation/duration/{uuid}")
    public ResponseEntity addLeave(@RequestBody String dateDuration, @PathVariable String uuid) throws JsonProcessingException {
        log.warn(dateDuration);
        List<WorkHours> listOfWorkHours = WorkHoursUtils.getDatesFromDuration(dateDuration, uuid, WorkHourType.LEAVE);
        workHoursService.addLeaveAsDuration(listOfWorkHours);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/leave/today/{uuid}")
    public ResponseEntity addLeaveToday(@PathVariable String uuid){
        workHoursService.addLeaveToday(uuid, WorkHourType.LEAVE);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/sick/today/{uuid}")
    public ResponseEntity addSickLeaveToday(@PathVariable String uuid){
        workHoursService.addLeaveToday(uuid, WorkHourType.SICK_LEAVE);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/today/log")
    public ResponseEntity<List<WorkHours>> logHoursOfCurrentDay(@RequestBody WorkhourLog[] logs){
        return new ResponseEntity<>(workHoursService.addTodaysLogs(logs), HttpStatus.OK);
    }
}
