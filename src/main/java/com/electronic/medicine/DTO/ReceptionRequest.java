package com.electronic.medicine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionRequest {

    private Long id;

    private String date;

    private String time;
}
