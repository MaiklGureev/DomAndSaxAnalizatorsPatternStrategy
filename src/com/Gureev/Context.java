package com.Gureev;

public class Context {
    private Analizator analizator;

    public Context(Analizator analizator) {
        this.analizator = analizator;
    }

    public Analizator getAnalizator() {
        return analizator;
    }

    public void setAnalizator(Analizator analizator) {
        this.analizator = analizator;
    }

    public void executeAnalizator(){
        analizator.checkAverageMark();
    }
}
