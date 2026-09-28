package dev.aziz.task2kyc.dto;

public record KycResult(
        boolean completed,
        String details
) {}
