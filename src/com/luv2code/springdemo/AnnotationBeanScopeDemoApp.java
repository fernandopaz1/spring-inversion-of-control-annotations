package com.luv2code.springdemo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AnnotationBeanScopeDemoApp {
    public static void main(String[] args) {
        // cargamos el archivo de configuraciones
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        //obtenemos el bean del contenedor de spring
        Coach myCoach = context.getBean("tennisCoach", Coach.class);

        Coach alphaCoach = context.getBean("tennisCoach", Coach.class);

        System.out.println("Son iguales: " + (myCoach==alphaCoach));

        System.out.println("\nMemory location of myCoach: "+myCoach);
        System.out.println("\nMemory location of alphaCoach: "+alphaCoach);

        // cerramos el archivo de configuraciones
        context.close();
    }
}
