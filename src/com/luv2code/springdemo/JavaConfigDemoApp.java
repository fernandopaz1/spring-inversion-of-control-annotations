package com.luv2code.springdemo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JavaConfigDemoApp {
    public static void main(String[] args) {
        // leemos ela archivo de configuraciones de spring
        // ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SportsConfig.class);
        
        Coach myCoach = context.getBean("swimCoach", Coach.class);

        System.out.println(myCoach.getDailyWorkout());
        System.out.println(myCoach.getDailyFortune());

        // cerramos el context
        context.close();
    }
}
