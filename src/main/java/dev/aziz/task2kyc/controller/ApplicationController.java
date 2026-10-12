package dev.aziz.task2kyc.controller;

import dev.aziz.task2kyc.dto.ApplicationRequest;
import dev.aziz.task2kyc.entity.Application;
import dev.aziz.task2kyc.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/complete-kyc")
    public ResponseEntity<Application> completeKyc(@RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.completeKyc(request.applicationId()));
    }

    @PostMapping
    public ResponseEntity<Application> createApplication(@RequestBody Application application) {
        return ResponseEntity.ok(applicationService.save(application));
    }

}

