package com.example.reco_test.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserPlaceReportResponseDTO {

    private UUID id;

    private String userId;

    private String placeType;

    private String name;

    private String district;

    private String address;

    private String detailAddress;

    private Double latitude;

    private Double longitude;

    private String description;

    private String status;

    private String geocodeStatus;

    private LocalDateTime createdAt;
}