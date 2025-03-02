package com.electronic.medicine.services;

import com.electronic.medicine.entity.Reception;
import com.electronic.medicine.repository.ReceptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceptionService {


    private final ReceptionRepository receptionRepository;

    public void saveReception(Reception reception) {
        receptionRepository.saveAndFlush(reception);
    }

}
