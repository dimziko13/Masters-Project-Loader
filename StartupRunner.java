package com.example.mastersapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private LogImporter importer;

    @Override
    public void run(String... args) throws Exception {

        importer.importAll(
            "C:/Users/dimzi/Desktop/masters_project/logs/logs/access.log",
            "C:/Users/dimzi/Desktop/masters_project/logs/logs/datareceiver.log",
            "C:/Users/dimzi/Desktop/masters_project/logs/logs/namesystem.log"
        );

        System.out.println("Log import completed.");
    }
}
