package dev.aziz.task2kyc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "outbox")
@NoArgsConstructor
public class OutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType;
    private UUID aggregateId;
    private String messageType;
    private String payload;
    private String clientId;
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;
    private Instant lockedUntil;

    public OutboxEntity(String aggregateType, UUID aggregateId, String messageType, String payload, String clientId, OutboxStatus status) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.messageType = messageType;
        this.payload = payload;
        this.clientId = clientId;
        this.status = status;
    }

}
