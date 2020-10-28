package com.sg.m4vmv2.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class VMAuditDAOImpl implements VMAuditDAO {

    public static final String AUDIT_FILE = "audit.txt";

    @Override
    public void writeAuditEntry(String entry) throws VendingPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new VendingPersistenceException("Could not persist stock/transaction record", e);
        }

        LocalDateTime timestamp = LocalDateTime.now();

        out.println(timestamp.toString() + " : " + entry);
        out.flush();
    }

}
