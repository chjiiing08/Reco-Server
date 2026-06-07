package com.example.reco_test.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AnalysisDTO {
    private UUID userId;
    private String imageUrl;
    private String itemName;
    private Boolean isRecyclable;
    private String disposalMethodSummary;
    private Double confidence;
}
