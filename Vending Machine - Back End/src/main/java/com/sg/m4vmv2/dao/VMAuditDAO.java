package com.sg.m4vmv2.dao;

public interface VMAuditDAO {

    /**
     * Create a time stamped entry of item addition or removal (via sale) from
     * inventory
     *
     * @param entry {String} record of stocking or sale of an item
     * @throws VendingPersistenceException if cannot write to audit file
     */
    public void writeAuditEntry(String entry) throws VendingPersistenceException;
}
