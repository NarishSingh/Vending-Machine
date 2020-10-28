/*
When user tries to purchase an item with 0 inventory
*/
package com.sg.m4vmv2.service;

/**
 *
 * @author naris
 */
public class NoSuchItemExistsException extends Exception {
    public NoSuchItemExistsException(String msg){
        super(msg);
    }
    
    public NoSuchItemExistsException(String msg, Throwable cause){
        super(msg, cause);
    }
}
