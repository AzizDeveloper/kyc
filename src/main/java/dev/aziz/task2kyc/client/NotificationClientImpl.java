package dev.aziz.task2kyc.client;

import dev.aziz.task2kyc.dto.NotificationResult;
import dev.aziz.task2kyc.dto.SmsRequest;
import org.springframework.stereotype.Component;

@Component
public class NotificationClientImpl implements NotificationClient {

    @Override
    public NotificationResult sendSms(SmsRequest request) {
        return new NotificationResult.Success("Everything is fine!");
    }

}
