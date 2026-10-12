package dev.aziz.task2kyc.client;

import dev.aziz.task2kyc.dto.KycResult;


public interface KycClient {

    KycResult fetchStatus(String kycSessionId);

}