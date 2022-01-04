package com.gacnik.diplomska.naloga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gacnik.diplomska.naloga.model.enums.Gender;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
@Document(collection = "employees")
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee {
    @Id
    private String uuid;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @Indexed(unique = true)
    @NotNull
    private String email;
    @Indexed(unique = true)
    @NotNull
    private String phone;
    @NotNull
    private Address address;
    @NotNull
    private Gender gender;
    @Indexed(unique = true, sparse = true)
    private ArrayList<String> deviceId;

    private ArrayList<SimpleGrantedAuthority> roles;
    private String password;

    public Employee(String name, String surname, String email, String phone, Address address, Gender gender, ArrayList<String> deviceId, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.deviceId = deviceId;
        this.password = password;
    }
}
