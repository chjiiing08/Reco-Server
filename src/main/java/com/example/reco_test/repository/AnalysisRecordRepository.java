package com.example.reco_test.repository;

import com.example.reco_test.entity.AnalysisRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

public interface AnalysisRecordRepository extends JpaRepository<AnalysisRecord, UUID> {

    long countByAnalyzedAtAfter(LocalDateTime dateTime);

    List<AnalysisRecord> findByAnalyzedAtAfter(LocalDateTime dateTime);

    long countByAnalyzedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<AnalysisRecord> findByUserId(UUID userId);

    List<AnalysisRecord> findByUserIdOrderByAnalyzedAtDesc(UUID userId);

    List<AnalysisRecord> findByUserIdAndAnalyzedAtAfter(
            UUID userId,
            LocalDateTime analyzedAt
    );

    long countByUserId(UUID userId);

    long countByUserIdAndAnalyzedAtAfter(
            UUID userId,
            LocalDateTime analyzedAt
    );

    long countByUserIdAndAnalyzedAtBetween(
            UUID userId,
            LocalDateTime start,
            LocalDateTime end
    );
}