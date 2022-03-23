package com.gacnik.diplomska.naloga.controller;

import com.gacnik.diplomska.naloga.service.MonthlyReportService;
import com.gacnik.diplomska.naloga.util.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/monthly")
@AllArgsConstructor
public class MonthlyReportController {

    private final MonthlyReportService monthlyReportService;
    private final JwtTokenUtil jwtTokenUtil;
    @GetMapping("/")
    public ResponseEntity<Map<String,Long>> getMonthlyReportOfEmployee(@RequestHeader(name="Authorization") String token){
        return new ResponseEntity<>(monthlyReportService.getMonthlyHoursForEmployee(jwtTokenUtil.getUsernameFromToken(token.substring(7))), HttpStatus.OK);
    }
}
