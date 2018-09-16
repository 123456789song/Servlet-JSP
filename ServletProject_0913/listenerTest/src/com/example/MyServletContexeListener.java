package com.example;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Class: listenerTest
 * @Author: Song
 * @Description:
 * @Create: 2018-09-13 20:31
 */
public class MyServletContexeListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event){
        ServletContext sc = event.getServletContext();
        String dogBreed = sc.getInitParameter("breed");
        Dog d = new Dog(dogBreed);
        sc.setAttribute("dog",d);
    }

}
