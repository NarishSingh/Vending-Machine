package com.sg.m4vmv2.dao;

public class VendingPersistenceException extends Exception {
    public VendingPersistenceException(String msg){
        super(msg);
    }
    
    public VendingPersistenceException(String msg, Throwable cause){
        super(msg, cause);
    }
}
