package dev.aziz.task2kyc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Task2kycApplication {

    public static void main(String[] args) {
        SpringApplication.run(Task2kycApplication.class, args);
    }

}
