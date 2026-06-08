package com.example.reco_test.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "analysis_records")
public class AnalysisRecord {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "is_recyclable")
    private Boolean isRecyclable;

    @Column(name = "disposal_method_summary", columnDefinition = "TEXT")
    private String disposalMethodSummary;

    @Column(name = "material_probabilities", columnDefinition = "TEXT")
    private String materialProbabilities;

    @Column(name = "contamination_level")
    private String contaminationLevel;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;
}