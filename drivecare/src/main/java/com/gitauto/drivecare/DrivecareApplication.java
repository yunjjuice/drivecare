package com.gitauto.drivecare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DrivecareApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrivecareApplication.class, args);
    }

}
