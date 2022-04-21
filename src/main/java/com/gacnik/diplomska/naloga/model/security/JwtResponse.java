package com.gacnik.diplomska.naloga.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
@AllArgsConstructor
public class JwtResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final String refreshToken;

}
