package com.luv2code.springdemo;

public class SwimCoach implements Coach{

    private FortuneService myFortuneService;

    public SwimCoach(FortuneService theFortuneService) {
        myFortuneService = theFortuneService;
    }

    @Override
    public String getDailyWorkout() {
        return "Nada";
    }

    @Override
    public String getDailyFortune() {
        return myFortuneService.getFortune();
    }

}
