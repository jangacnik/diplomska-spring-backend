package com.gacnik.diplomska.naloga.service;

import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.model.MacRequest;
import com.gacnik.diplomska.naloga.model.enums.WorkHourType;
import com.gacnik.diplomska.naloga.repo.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeRedService {
    @Autowired
    private WorkHoursService workHoursService;
    @Autowired
    private EmployeeRepository employeeRepository;
    public void recordTimeByNetworkDevices(MacRequest macRequest) throws Exception {
        for(String macAddress: macRequest.getNewDevices()) {
            List<Employee> employeeList = employeeRepository.findEmployeeByDeviceIdMac(macAddress);
            if(!employeeList.isEmpty()) {
                if(workHoursService.isBreakActive(employeeList.get(0).getUuid())) {
                    workHoursService.endBreak(employeeList.get(0).getUuid());
                } else {
                    workHoursService.addNewEntry(employeeList.get(0).getUuid(), WorkHourType.WORK);
                }
            }
        }
        for (String macAddress: macRequest.getOldDevices()) {
            List<Employee> employeeList = employeeRepository.findEmployeeByDeviceIdMac(macAddress);
            if(!employeeList.isEmpty()) {
                if(workHoursService.isFirstBreak(employeeList.get(0).getUuid())) {
                    workHoursService.addNewBreak(employeeList.get(0).getUuid());
                } else if (!workHoursService.isBreakActive(employeeList.get(0).getUuid())){
                    workHoursService.endEntry(employeeList.get(0).getUuid());
                }
            }
        }
    }
}
