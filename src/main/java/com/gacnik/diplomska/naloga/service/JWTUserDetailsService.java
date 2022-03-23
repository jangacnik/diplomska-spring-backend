package com.gacnik.diplomska.naloga.service;

import com.gacnik.diplomska.naloga.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JWTUserDetailsService implements UserDetailsService {
    @Autowired
    private EmployeeService employeeService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (employeeService.getEmployeeByEmailAuth(s) != null) {
            final Employee employee = employeeService.getEmployeeByEmailAuth(s);
            return new User(employee.getEmail(), employee.getPassword(),
                    employee.getRoles());

        } else {
            throw new UsernameNotFoundException("User not found with username: " + s);
        }
    }
}
