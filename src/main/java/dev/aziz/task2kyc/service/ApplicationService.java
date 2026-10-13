package dev.aziz.task2kyc.service;

import dev.aziz.task2kyc.client.KycClient;
import dev.aziz.task2kyc.dto.KycResult;
import dev.aziz.task2kyc.entity.Application;
import dev.aziz.task2kyc.entity.ApplicationStatus;
import dev.aziz.task2kyc.entity.OutboxEntity;
import dev.aziz.task2kyc.entity.OutboxStatus;
import dev.aziz.task2kyc.repository.ApplicationRepository;
import dev.aziz.task2kyc.repository.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final OutboxMessageRepository outboxMessageRepository;
    private final KycClient kycClient;
    private final TransactionTemplate transactionTemplate;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Application completeKyc(UUID applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application with id: " + applicationId + " not found."));
        if (!ApplicationStatus.KYC_PENDING.equals(application.getStatus())) {
            throw new RuntimeException("Application is not KYC pending.");
        }
        KycResult kycResult = kycClient.fetchStatus(application.getKycSessionId());
        if (!kycResult.completed()) {
            throw new RuntimeException("KYC status is not completed.");
        }

        Application updated = transactionTemplate.execute(status -> {
            Application application1 = applicationRepository.findByIdForUpdate(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application with id: " + applicationId + " not found."));

            if (!ApplicationStatus.KYC_PENDING.equals(application1.getStatus())) {
                throw new RuntimeException("Application is not KYC pending.");
            }
            application1.setStatus(ApplicationStatus.KYC_COMPLETED);
            OutboxEntity outboxEntity = new OutboxEntity(
                    "Application",
                    application1.getId(),
                    "ApplicationUpdated",
                    convertToJson(application1),
                    application1.getClientId().toString(),
                    OutboxStatus.INITIAL
            );
            outboxMessageRepository.save(outboxEntity);

            return application1;
        });

        return updated;
    }

    private String convertToJson(Application message) {
        return OBJECT_MAPPER.writeValueAsString(message);
    }

    public Application save(Application application) {
        return applicationRepository.save(application);
    }
}
