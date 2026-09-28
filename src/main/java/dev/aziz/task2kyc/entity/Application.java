package dev.aziz.task2kyc.entity;

import java.util.UUID;

public record Application(
        UUID id,
        UUID clientId,
        ApplicationStatus status,
        String kycSessionId
) {}
