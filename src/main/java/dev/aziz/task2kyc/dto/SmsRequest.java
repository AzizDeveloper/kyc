package dev.aziz.task2kyc.dto;

public record SmsRequest(
        String clientId,
        String text
) {}
