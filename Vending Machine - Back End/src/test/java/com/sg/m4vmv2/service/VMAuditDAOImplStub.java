package com.sg.m4vmv2.service;

import com.sg.m4vmv2.dao.VMAuditDAO;
import com.sg.m4vmv2.dao.VendingPersistenceException;

public class VMAuditDAOImplStub implements VMAuditDAO {
    
    @Override
    public void writeAuditEntry(String entry) throws VendingPersistenceException{
        //do nothing
    }
}
