package com.gacnik.diplomska.naloga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gacnik.diplomska.naloga.util.UuidGenerator;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Log4j2
@Data
@Document(collection = "employees")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {
    @Id
    private String uuid;
    private String name;
    private String surname;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String phone;
    private Address address;
    private Sex sex;
    @Indexed(unique = true)
    private List<String> deviceId;

    public Employee(String name, String surname, String email, String phone, Address address, Sex sex, List<String> deviceId) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.sex = sex;
        this.deviceId = deviceId;
    }
}
