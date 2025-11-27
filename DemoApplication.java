package com.example.mastersapp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import com.example.Utilities.LogImporter;
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
    	
    	    System.out.println(">>> Using DB URL: " + System.getProperty("spring.datasource.url"));
    	

        SpringApplication.run(DemoApplication.class, args);
    }
    
}
