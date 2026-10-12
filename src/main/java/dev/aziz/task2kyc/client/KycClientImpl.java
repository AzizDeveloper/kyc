package dev.aziz.task2kyc.client;

import dev.aziz.task2kyc.dto.KycResult;
import org.springframework.stereotype.Component;

@Component
public class KycClientImpl implements KycClient {

    @Override
    public KycResult fetchStatus(String kycSessionId) {
        return new  KycResult(true, "KYC details: success!");
    }

}
