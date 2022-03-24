package com.gacnik.diplomska.naloga.controller;

import com.gacnik.diplomska.naloga.model.*;
import com.gacnik.diplomska.naloga.model.enums.WorkHourType;
import com.gacnik.diplomska.naloga.service.EmployeeService;
import com.gacnik.diplomska.naloga.service.WorkHoursService;
import com.gacnik.diplomska.naloga.util.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
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

    @PostMapping(value = "/break/start/{uuid}")
    public ResponseEntity<String> addNewBreak(@PathVariable String uuid) throws Exception {
        return new ResponseEntity<>(workHoursService.addNewBreak(uuid), HttpStatus.OK);
    }

    @PutMapping("/break/end/{uuid}")
    public ResponseEntity<String> endBreak(@PathVariable String uuid) throws Exception {
        return new ResponseEntity<>(workHoursService.endBreak(uuid), HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> workStatus(@RequestHeader (name="Authorization") String token) {
        Employee employee = employeeService.getEmployeeByEmail(jwtTokenUtil.getUsernameFromToken(token.substring(7)));
        return new ResponseEntity<Boolean>(workHoursService.getEmployeeStatus(employee.getUuid()), HttpStatus.OK);
    }


    @GetMapping("/week/current")
    public ResponseEntity<Map<DayOfWeek, Long>> getCurrentWeekWorkHours(@RequestHeader (name="Authorization") String token) {
        Employee employee = employeeService.getEmployeeByEmail(jwtTokenUtil.getUsernameFromToken(token.substring(7)));
        return new ResponseEntity<>(workHoursService.getWorkhoursOfCurrentWeek(employee.getUuid()), HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<String> getCurrentWorkTime(@RequestHeader (name="Authorization") String token) {
        Employee employee = employeeService.getEmployeeByEmail(jwtTokenUtil.getUsernameFromToken(token.substring(7)));
        String calculatedWorkTime = workHoursService.calculateCurrentWorkTime(employee.getUuid());
        return new ResponseEntity<>(calculatedWorkTime, HttpStatus.OK);
    }
}
