package com.sg.m4vmv2;

import com.sg.m4vmv2.controller.VMController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {
        /*
        UserIO io = new UserIOImpl();
        VMView v = new VMView(io);
        
        VMDAO dao = new VMDAOImpl();
        VMAuditDAO auditDAO = new VMAuditDAOImpl();
        VMService serv = new VMServiceImpl(dao, auditDAO);
        
        VMController c = new VMController(serv, v);
        
        c.run();
         */
        
        ApplicationContext actx = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        VMController c = actx.getBean("controller", VMController.class);
        
        c.run();
    }

}
