package dev.aziz.task2kyc.dto;

import java.util.UUID;

public record ApplicationRequest(
    UUID applicationId,
    UUID clientId
) {}
