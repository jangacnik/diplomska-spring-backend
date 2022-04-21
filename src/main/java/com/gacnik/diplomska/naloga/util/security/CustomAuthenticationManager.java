package com.gacnik.diplomska.naloga.util.security;

import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationManager  implements AuthenticationManager {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        final Employee employee = employeeService.getEmployeeByEmailAuth(username);
        if(passwordEncoder.matches(password, employee.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, employee.getRoles());
        } else {
            throw new BadCredentialsException("1000");
        }
    }
}
