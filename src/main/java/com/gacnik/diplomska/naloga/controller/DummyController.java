package com.gacnik.diplomska.naloga.controller;

import com.gacnik.diplomska.naloga.service.EmployeeService;
import com.gacnik.diplomska.naloga.service.MonthlyHoursService;
import com.gacnik.diplomska.naloga.service.WorkHoursService;
import com.gacnik.diplomska.naloga.util.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/dummy")
@AllArgsConstructor
public class DummyController {
    private final WorkHoursService workHoursService;
    private final MonthlyHoursService monthlyHoursService;
    @Autowired
    private final JwtTokenUtil jwtTokenUtil;
    private final EmployeeService employeeService;

    @PostMapping("/workhour/{employeeId}/{month}/{year}")
    public ResponseEntity<Boolean> createTestData(@PathVariable String employeeId, @PathVariable int month, @PathVariable int year) {
        workHoursService.createTestData(employeeId,month,year);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/calcMonth/{month}/{year}")
    public ResponseEntity<Boolean> calcMonthlyHours(@PathVariable String month, @PathVariable String year) {
        monthlyHoursService.calculateMonthlyHoursDummy(month,year);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
