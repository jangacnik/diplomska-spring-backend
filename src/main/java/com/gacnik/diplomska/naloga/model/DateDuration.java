package com.gacnik.diplomska.naloga.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateDuration {
    @JsonFormat(pattern = "dd/MM/yyyy@HH:mm:ss")
    @DateTimeFormat(pattern = "dd/MM/yyyy@HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern = "dd/MM/yyyy@HH:mm:ss")
    @DateTimeFormat(pattern = "dd/MM/yyyy@HH:mm:ss")
    private Date endDate;
}
