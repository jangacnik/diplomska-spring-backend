package com.gacnik.diplomska.naloga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MacRequest {
    private String[] newDevices;
    private String[] oldDevices;
}
