package com.luv2code.springdemo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AnnotationDemoApp {
    public static void main(String[] args) {
        // leemos ela archivo de configuraciones de spring
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        System.out.println("-".repeat(10)+"Constructor Inyection"+"-".repeat(10));
        // obtenemos el bean del contenedor de spring
        // si en @Component no especificamos el bean Id entonces el default 
        // es el mismo que el nombre de la clase con la primer letra en minuscula
        Coach myCoach = context.getBean("tennisCoach",Coach.class);

        // llamamos un metodo del bean
        System.out.println(myCoach.getDailyWorkout());

        // lamamos al metodo inyectaqdo en el constructor
        System.out.println(myCoach.getDailyFortune());

        System.out.println("-".repeat(10)+"Setter Inyection"+"-".repeat(10));
        // Lo mismo pero con setter inyection
        Coach myChessCoach = context.getBean("chessCoach",Coach.class);

        // llamamos un metodo del bean
        System.out.println(myChessCoach.getDailyWorkout());

        // lamamos al metodo inyectaqdo en el constructor
        System.out.println(myChessCoach.getDailyFortune());
    
        System.out.println("-".repeat(10)+"Field Inyection"+"-".repeat(10));

        // Lo mismo pero con setter inyection
        Coach myGolfCoach = context.getBean("golfCoach",Coach.class);

        // llamamos un metodo del bean
        System.out.println(myGolfCoach.getDailyWorkout());

        // lamamos al metodo inyectaqdo en el constructor
        System.out.println(myGolfCoach.getDailyFortune());

        // cerramos el context
        context.close();
    }
}
