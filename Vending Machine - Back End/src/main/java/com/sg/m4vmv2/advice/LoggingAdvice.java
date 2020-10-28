package com.sg.m4vmv2.advice;

import com.sg.m4vmv2.dao.VMAuditDAO;
import com.sg.m4vmv2.dao.VendingPersistenceException;
import org.aspectj.lang.JoinPoint;

public class LoggingAdvice {

    VMAuditDAO auditDAO;

    public LoggingAdvice(VMAuditDAO auditDAO) {
        this.auditDAO = auditDAO;
    }

    /**
     * Create an audit entry that logs the method call and its arguments - used
     * in Spring AOP
     *
     * @param jp {JoinPoint} the method called at the pointcuts
     */
    public void createAuditEntry(JoinPoint jp) {
        Object[] args = jp.getArgs(); //gets the parameters
        String auditEntry = jp.getSignature().getName() + ": ";

        for (Object currentArg : args) {
            auditEntry += currentArg;
        }

        try {
            auditDAO.writeAuditEntry(auditEntry);
        } catch (VendingPersistenceException e) {
            System.err.println("ERROR: Could not create audit entry in LoggingAdvice.");
        }
    }

    /**
     * Create and audit entry that logs the exceptions thrown at any point in
     * the program - used in Spring AOP
     *
     * @param ex {Exception} the exception thrown by any method in the program
     */
    public void createExceptionEntry(Exception ex) {
        String auditEntry = "Thrown: " + ex.getMessage();

        try {
            auditDAO.writeAuditEntry(auditEntry);
        } catch (VendingPersistenceException e) {
            System.err.println("ERROR: could not write audit entry in LoggingAdvice.");
        }
    }

    /**
     * Create and audit entry that logs the exceptions thrown and its joinpoint
     * at any point in the program - used in Spring AOP
     *
     * @param jp {JoinPoint} the method at which the exception it thrown
     * @param ex {Exception} the exception thrown by any method in the program
     */
    public void createExceptionWithJPEntry(JoinPoint jp, Exception ex) {
        String auditEntry = "Exception Thrown in: " + jp.getSignature().getName() + ":: Message: " + ex.getMessage();

        try {
            auditDAO.writeAuditEntry(auditEntry);
        } catch (VendingPersistenceException e) {
            System.err.println("ERROR: could not write audit entry in LoggingAdvice.");
        }
    }
}
