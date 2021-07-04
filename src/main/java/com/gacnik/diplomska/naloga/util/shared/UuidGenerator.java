package com.gacnik.diplomska.naloga.util.shared;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UuidGenerator {
    public String generateUuid() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
