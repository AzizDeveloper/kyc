package dev.aziz.task2kyc.service;

import dev.aziz.task2kyc.client.NotificationClient;
import dev.aziz.task2kyc.dto.NotificationResult;
import dev.aziz.task2kyc.dto.SmsRequest;
import dev.aziz.task2kyc.entity.OutboxEntity;
import dev.aziz.task2kyc.entity.OutboxStatus;
import dev.aziz.task2kyc.repository.OutboxMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class OutboxScheduler {

    @Value("${application.blocking.lease-duration}")
    private Duration blockingLease;
    @Value("${application.blocking.lease-duration:20}")
    private int batchSize;

    private final TransactionTemplate transactionTemplate;
    private final OutboxMessageRepository outboxRepository;
    private final NotificationClient publisher;

    public OutboxScheduler(
            TransactionTemplate transactionTemplate,
            OutboxMessageRepository outboxRepository,
            NotificationClient publisher
    ) {
        this.transactionTemplate = transactionTemplate;
        this.outboxRepository = outboxRepository;
        this.publisher = publisher;
    }

    @Scheduled(fixedDelayString = "${outbox.poll-delay:5000}")
    public void processOutbox() {
        List<OutboxEntity> entities = getOutboxEntities(batchSize);

        if (entities.isEmpty()) {
            return;
        }

        for (OutboxEntity entity : entities) {
            processSingleEntity(entity);
        }
    }

    private List<OutboxEntity> getOutboxEntities(int batchSize) {
        Instant now = Instant.now();
        Instant leaseUntil = now.plus(blockingLease);

        return transactionTemplate.execute(status -> {
            List<OutboxEntity> entities = outboxRepository.findClaimableRows(now, batchSize);

            for (OutboxEntity entity : entities) {
                entity.setStatus(OutboxStatus.PROCESSING);
                entity.setLockedUntil(leaseUntil);
            }

            outboxRepository.saveAll(entities);

            return entities;
        });
    }

    private void processSingleEntity(OutboxEntity entity) {
        try {
            NotificationResult notificationResult =
                    publisher.sendSms(new SmsRequest(entity.getClientId(), "KYC notification sent."));
            if (notificationResult instanceof  NotificationResult.Success) {
                transactionTemplate.executeWithoutResult(status -> {
                    outboxRepository.markAsSent(entity.getId());
                });
            } else if (notificationResult instanceof  NotificationResult.ValidationError) {
                String reason = ((NotificationResult.ValidationError) notificationResult).reason();
                transactionTemplate.executeWithoutResult(status -> {
                    outboxRepository.markAsDead(entity.getId(), reason);
                });
                log.error(reason);
            }
        }  catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
    }

}