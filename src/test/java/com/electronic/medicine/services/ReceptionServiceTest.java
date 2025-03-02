package com.electronic.medicine.services;

import com.electronic.medicine.entity.Reception;
import com.electronic.medicine.exception.MedicineServerErrorException;
import com.electronic.medicine.repository.ReceptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceptionServiceTest {

    @InjectMocks
    private ReceptionService receptionService;
    @Mock
    private ReceptionRepository receptionRepository;

    @Test
    void loadReceptionServiceContext() {
        assertNotNull(this.receptionRepository);
        assertNotNull(this.receptionService);
    }

    @Test
    @DisplayName("saveReception успешное сохранение")
    void saveReception_Success() {
        Reception reception = mock(Reception.class);
        assertNotNull(reception);
        when(this.receptionRepository.saveAndFlush(reception)).thenReturn(reception);
        var result = this.receptionRepository.saveAndFlush(reception);
        assertEquals(reception,result);
        verify(this.receptionRepository).saveAndFlush(reception);
        verifyNoMoreInteractions(this.receptionRepository);
    }
}