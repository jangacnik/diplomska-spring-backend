package com.gacnik.diplomska.naloga.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gacnik.diplomska.naloga.model.*;
import com.gacnik.diplomska.naloga.model.enums.WorkHourType;
import com.gacnik.diplomska.naloga.service.EmployeeService;
import com.gacnik.diplomska.naloga.service.WorkHoursService;
import com.gacnik.diplomska.naloga.util.security.JwtTokenUtil;
import com.gacnik.diplomska.naloga.util.shared.WorkHoursUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/hours")
@AllArgsConstructor
public class WorkHoursController {

    private final WorkHoursService workHoursService;
    @Autowired
    private final JwtTokenUtil jwtTokenUtil;
    private final EmployeeService employeeService;

    @PostMapping(value = "/new/{uuid}/{type}")
    public ResponseEntity<String> addNewEntry(@PathVariable String uuid, @PathVariable WorkHourType type) {
        return new ResponseEntity<>(workHoursService.addNewEntry(uuid, type), HttpStatus.OK);
    }

    @PutMapping("/end/{uuid}")
    public ResponseEntity<MonthlyWorkHours> endEntry(@PathVariable String uuid) throws Exception {
        return new ResponseEntity<>(workHoursService.endEntry(uuid), HttpStatus.OK);
    }


    @GetMapping("/week/current")
    public ResponseEntity<Map<DayOfWeek, Long>> getCurrentWeekWorkHours(@RequestHeader (name="Authorization") String token) {
        Employee employee = employeeService.getEmployeeByEmail(jwtTokenUtil.getUsernameFromToken(token.substring(7)));
        return new ResponseEntity<>(workHoursService.getWorkhoursOfCurrentWeek(employee.getUuid()), HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<String> getCurrentWorkTime(@RequestHeader (name="Authorization") String token) {
        Employee employee = employeeService.getEmployeeByEmail(jwtTokenUtil.getUsernameFromToken(token.substring(7)));
        String t = workHoursService.calculateCurrentWorkTime(employee.getUuid());
        return new ResponseEntity<>(t, HttpStatus.OK);
    }
//
//    @PostMapping(value = "/sick/duration/{uuid}")
//    public ResponseEntity<Void> addSickLeave(@RequestBody String dateDuration, @PathVariable String uuid) throws JsonProcessingException {
//        List<WorkHours> listOfWorkHours = WorkHoursUtils.getDatesFromDuration(dateDuration, uuid, WorkHourType.SICK_LEAVE);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/vacation/duration/{uuid}")
//    public ResponseEntity<Void> addLeave(@RequestBody String dateDuration, @PathVariable String uuid) throws JsonProcessingException {
//        List<WorkHours> listOfWorkHours = WorkHoursUtils.getDatesFromDuration(dateDuration, uuid, WorkHourType.LEAVE);
//        workHoursService.addLeaveAsDuration(listOfWorkHours);
//        return new ResponseEntity<>(HttpStatus.ACCEPTED);
//    }
//
//    @PostMapping(value = "/leave/today/{uuid}")
//    public ResponseEntity<Void> addLeaveToday(@PathVariable String uuid) {
//        workHoursService.addLeaveToday(uuid, WorkHourType.LEAVE);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/sick/today/{uuid}")
//    public ResponseEntity<Void> addSickLeaveToday(@PathVariable String uuid) {
//        workHoursService.addLeaveToday(uuid, WorkHourType.SICK_LEAVE);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/today/log")
//    public ResponseEntity<List<WorkHours>> logHoursOfCurrentDay(@RequestBody WorkhourLog[] logs) {
//        return new ResponseEntity<>(workHoursService.addTodaysLogs(logs), HttpStatus.OK);
//    }
//
//    // put for edit
//    @PutMapping(value = "/edit/{uuid}")
//    ResponseEntity<WorkHours> editWorkHoursOfEmployee(@PathVariable String uuid) {
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//
//    // delete for deletion by id
//    @DeleteMapping(value = "/delete/{id}")
//    public ResponseEntity<Void> deleteWorkHoursById(@PathVariable String id) {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    // get for workhours by employee
//    @GetMapping(value = "/{uuid}")
//    public ResponseEntity<List<WorkHours>> getWorkHoursOfEmployee(@PathVariable String uuid) {
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//
//    // get for workhoours by employee between dates
//    @GetMapping(value = "/between/{uuid}")
//    public ResponseEntity<List<WorkHours>> getWorkHoursOfEmployeeBetweenDates(@PathVariable String uuid, @RequestBody DateDuration duration) {
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//    // get for workhours by employe in specific month
//
//    @GetMapping(value = "/month/{uuid}")
//    public ResponseEntity<List<WorkHours>> getWorkHoursOfEmployeeByMonth(@PathVariable String uuid, @RequestBody LocalDateTime date) {
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }


}
