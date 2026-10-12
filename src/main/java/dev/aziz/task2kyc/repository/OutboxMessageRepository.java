package dev.aziz.task2kyc.repository;

import dev.aziz.task2kyc.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface OutboxMessageRepository extends JpaRepository<OutboxEntity,Long> {

    List<OutboxEntity> findByPublished(boolean b);

    @Query(value = "SELECT * FROM outbox WHERE status = 'INITIAL' OR (status = 'PROCESSING' AND locked_until < :now) " +
            "ORDER BY id LIMIT :batchSize FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<OutboxEntity> findClaimableRows(@Param("now") Instant now, @Param("batchSize") int batchSize);

    @Modifying
    @Transactional
    @Query(value = "UPDATE outbox SET status = 'COMPLETED' WHERE id = :id", nativeQuery = true)
    void markAsSent(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE outbox SET status = 'FAILED', last_error = :error WHERE id = :id", nativeQuery = true)
    void markAsDead(@Param("id") Long id, @Param("error") String error);

}