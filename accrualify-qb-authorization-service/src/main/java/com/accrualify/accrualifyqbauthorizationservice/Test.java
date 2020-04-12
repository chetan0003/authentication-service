package com.accrualify.accrualifyqbauthorizationservice;

import java.util.Date;

public class Test {
    public static void main(String[] args) {
        Date date=new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10);//10 hour
        System.out.println(date);
    }
}
