package dev.aziz.task2kyc.repository;

import dev.aziz.task2kyc.entity.Application;

import java.util.UUID;

public interface ApplicationRepository {

    Application findById(UUID id);
    Application update(Application application);

}
