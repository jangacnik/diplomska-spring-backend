package com.gacnik.diplomska.naloga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Address {
    private String street;
    private String postalCode;
    private String city;
    private String country;
}
