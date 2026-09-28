package dev.aziz.task2kyc.client;

import dev.aziz.task2kyc.dto.NotificationResult;
import dev.aziz.task2kyc.dto.SmsRequest;

public interface NotificationClient {

    NotificationResult sendSms(SmsRequest request);
}
