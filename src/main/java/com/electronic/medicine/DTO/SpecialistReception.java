package com.electronic.medicine.DTO;

import com.electronic.medicine.entity.Reception;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class SpecialistReception {

    private Long specId;

    private List<ReceptionDto> list;

    public SpecialistReception(Long id, List<ReceptionDto> list) {
        this.specId = id;
        this.list = list.stream().map(reception -> new ReceptionDto(reception.getDate())).toList();
    }
}
