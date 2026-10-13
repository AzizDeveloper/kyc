package dev.aziz.task2kyc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "applications")
public class Application{
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;
        private UUID clientId;
        @Enumerated(EnumType.STRING)
        private ApplicationStatus status;
        private String kycSessionId;
}
