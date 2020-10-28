package com.sg.m4vmv2.service;

public class ItemDataValidationException extends Exception {

    public ItemDataValidationException(String msg) {
        super(msg);
    }

    public ItemDataValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
