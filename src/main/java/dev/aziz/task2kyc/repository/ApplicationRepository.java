package dev.aziz.task2kyc.repository;

import dev.aziz.task2kyc.entity.Application;
import dev.aziz.task2kyc.entity.ApplicationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT a
                FROM Application a
                WHERE a.id = :id
                  AND a.status = :status
            """)
    Optional<Application> findByIdAndStatusForUpdate(
            UUID id,
            ApplicationStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT a
                FROM Application a
                WHERE a.id = :id
            """)
    Optional<Application> findByIdForUpdate(UUID id);

//    Application update(Application application);

}
