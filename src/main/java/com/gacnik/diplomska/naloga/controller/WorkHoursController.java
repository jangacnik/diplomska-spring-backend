package com.gacnik.diplomska.naloga.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gacnik.diplomska.naloga.model.DateDuration;
import com.gacnik.diplomska.naloga.model.WorkHourType;
import com.gacnik.diplomska.naloga.model.WorkHours;
import com.gacnik.diplomska.naloga.service.WorkHoursService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

@RestController
@RequestMapping("api/v1/hours")
@AllArgsConstructor
@Log4j2
public class WorkHoursController {

    private final Gson gson;
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

    @PostMapping(value = "/sick")
    public void addSickLeave(@RequestBody DateDuration dateDuration) {
        log.error(dateDuration.getEndDate());
        log.error(dateDuration.getStartDate());
    }
}
