package dev.aziz.task2kyc.service;

import dev.aziz.task2kyc.client.KycClient;
import dev.aziz.task2kyc.client.NotificationClient;
import dev.aziz.task2kyc.dto.KycResult;
import dev.aziz.task2kyc.dto.NotificationResult;
import dev.aziz.task2kyc.dto.SmsRequest;
import dev.aziz.task2kyc.entity.Application;
import dev.aziz.task2kyc.entity.ApplicationStatus;
import dev.aziz.task2kyc.repository.ApplicationRepository;

import java.util.UUID;

public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final KycClient kycClient;
    private final NotificationClient notificationClient;

    public ApplicationService(ApplicationRepository applicationRepository,
                                  KycClient kycClient,
                                  NotificationClient notificationClient) {
        this.applicationRepository = applicationRepository;
        this.kycClient = kycClient;
        this.notificationClient = notificationClient;
    }

    public Application completeKyc(UUID applicationId, UUID clientId) {
        Application application = applicationRepository.findById(applicationId);
        if (!ApplicationStatus.KYC_PENDING.equals(application.status())) {
            throw new RuntimeException("Application is not KYC pending.");
        }
        KycResult kycResult = kycClient.fetchStatus(application.kycSessionId());
        if (!kycResult.completed()) {
            throw new RuntimeException("KYC status is not completed.");
        }
        Application newApplication = new Application(application.id(), application.clientId(),
                ApplicationStatus.KYC_COMPLETED, application.kycSessionId());
        Application updated = applicationRepository.update(newApplication);

        NotificationResult notificationResult = notificationClient.sendSms(
                new SmsRequest(
                        newApplication.clientId().toString(), "Your application have been completed."
                )
        );


        return updated;
    }
}
