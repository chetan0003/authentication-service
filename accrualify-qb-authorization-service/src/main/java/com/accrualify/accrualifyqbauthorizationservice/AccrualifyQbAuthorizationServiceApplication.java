package com.accrualify.accrualifyqbauthorizationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com**")
public class AccrualifyQbAuthorizationServiceApplication {

     public static void main(String[] args) {
        SpringApplication.run(AccrualifyQbAuthorizationServiceApplication.class, args);
    }





}