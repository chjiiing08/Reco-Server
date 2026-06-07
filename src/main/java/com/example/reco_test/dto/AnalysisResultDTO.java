package com.example.reco_test.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AnalysisResultDTO {

    private UUID analysisId;
    private String resultType;
    private String message;
    private String itemName;
    private Boolean isRecyclable;
    private String disposalMethodSummary;
}