package com.luv2code.springdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChessCoach implements Coach {

    private FortuneService fortuneService;

    public ChessCoach() {

    }

    @Autowired
    public void setFortuneService(FortuneService theFortuneService) {
        fortuneService = theFortuneService;
    }


    @Override
    public String getDailyWorkout() {
        return "Tactics, tactics, tactics";
    }

    @Override
    public String getDailyFortune() {
        return fortuneService.getFortune();
    }
    
}
