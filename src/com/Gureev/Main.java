package com.Gureev;

public class Main {

    public static void main(String[] args) {
	// write your code here

        Analizator analizator = new SAX();
        Context context =  new Context(analizator);
        context.executeAnalizator();

    }
}
