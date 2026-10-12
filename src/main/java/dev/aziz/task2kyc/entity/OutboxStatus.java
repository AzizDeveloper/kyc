package dev.aziz.task2kyc.entity;

public enum OutboxStatus {
    INITIAL,
    PROCESSING,
    COMPLETED,
    FAILED
}
