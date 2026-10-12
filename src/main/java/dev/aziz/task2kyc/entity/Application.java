package dev.aziz.task2kyc.entity;

import jakarta.persistence.Entity;
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
        private UUID id;
        private UUID clientId;
        private ApplicationStatus status;
        private String kycSessionId;
}
