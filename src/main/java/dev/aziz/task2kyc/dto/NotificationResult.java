package dev.aziz.task2kyc.dto;

public sealed interface NotificationResult permits
        NotificationResult.Success,
        NotificationResult.ValidationError {
    record Success(String reason) implements NotificationResult {}
    record ValidationError(String reason) implements NotificationResult {}
}
