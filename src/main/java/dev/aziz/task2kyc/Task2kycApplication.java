package dev.aziz.task2kyc;

import dev.aziz.task2kyc.entity.Application;
import dev.aziz.task2kyc.entity.ApplicationStatus;
import dev.aziz.task2kyc.repository.ApplicationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class Task2kycApplication {

    public static void main(String[] args) {
        SpringApplication.run(Task2kycApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationRepository applicationRepository) {
        return args -> {
            Application application1 = new  Application();
            application1.setClientId(UUID.randomUUID());
            application1.setStatus(ApplicationStatus.KYC_PENDING);
            application1.setKycSessionId(UUID.randomUUID().toString());
            applicationRepository.save(application1);

            Application application2 = new  Application();
            application2.setClientId(UUID.randomUUID());
            application2.setStatus(ApplicationStatus.KYC_PENDING);
            application2.setKycSessionId(UUID.randomUUID().toString());
            applicationRepository.save(application2);

            Application application3 = new  Application();
            application3.setClientId(UUID.randomUUID());
            application3.setStatus(ApplicationStatus.KYC_PENDING);
            application3.setKycSessionId(UUID.randomUUID().toString());
            applicationRepository.save(application3);
        };
    }

}
