package com.gacnik.diplomska.naloga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gacnik.diplomska.naloga.model.enums.Sex;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
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
