package com.gacnik.diplomska.naloga.advice;
import com.gacnik.diplomska.naloga.controller.WorkHoursController;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = {WorkHoursController.class})
public class WorkHoursAdvice {
}
