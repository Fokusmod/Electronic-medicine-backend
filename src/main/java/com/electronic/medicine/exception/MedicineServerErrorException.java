package com.electronic.medicine.exception;

public class MedicineServerErrorException extends RuntimeException{
    public MedicineServerErrorException(String msg) {
        super(msg);
    }
}
