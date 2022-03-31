package com.luv2code.springdemo;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TennisCoach implements Coach {

    private FortuneService fortuneService;
    
    @Autowired
    public TennisCoach(@Qualifier("randomFortuneService") FortuneService theFortuneService) {
        fortuneService = theFortuneService;
    }

    @Override
    public String getDailyWorkout() {
        return "Practice your backhand volley";
    }

    @Override
    public String getDailyFortune() {
        return fortuneService.getFortune();
    }

    @PostConstruct
    public void doMyStartupStuff() {
        System.out.println("Tennis coach startup stuff");
    }

    @PreDestroy
    public void doMyCleanupStuff() {
        System.out.println("Tennis coach cleanup stuff");
    }
}
