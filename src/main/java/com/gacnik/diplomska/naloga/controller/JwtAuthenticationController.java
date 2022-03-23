package com.gacnik.diplomska.naloga.controller;

import com.gacnik.diplomska.naloga.model.security.JwtRequest;
import com.gacnik.diplomska.naloga.model.security.JwtResponse;
import com.gacnik.diplomska.naloga.service.JWTUserDetailsService;
import com.gacnik.diplomska.naloga.util.security.CustomAuthenticationManager;
import com.gacnik.diplomska.naloga.util.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private CustomAuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JWTUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationController.class);


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
        log.info("Authentication token created for user {}", userDetails.getUsername());
        return ResponseEntity.ok(new JwtResponse(token, refreshToken));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshToken(@RequestHeader String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(token, userDetails)) {
            final String newToken = jwtTokenUtil.generateToken(userDetails);
            final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
            log.info("User {} requested refresh of authentication token", username);
            return ResponseEntity.ok(new JwtResponse(newToken, refreshToken));
        }
        log.warn("User {} requested refresh of token, but was unauthorized", username);
        return ResponseEntity.badRequest().body(new ResponseEntity<String>(HttpStatus.UNAUTHORIZED));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
