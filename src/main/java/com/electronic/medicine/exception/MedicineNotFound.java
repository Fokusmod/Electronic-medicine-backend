package com.electronic.medicine.exception;


import lombok.NoArgsConstructor;


@NoArgsConstructor
public class MedicineNotFound extends RuntimeException {

    public MedicineNotFound(String msg) {
        super(msg);
    }
}
