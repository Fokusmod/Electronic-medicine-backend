package com.electronic.medicine.exception;

public class MedicineBadCredential extends RuntimeException{

    public MedicineBadCredential(String msg) {
        super(msg);
    }
}
