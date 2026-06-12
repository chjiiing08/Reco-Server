package com.example.reco_test.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder

public class DisposalPlaceResponseDTO {
    private UUID id;
    private String name;
    private String placeType;
    private String district;
    private String address;
    private Double latitude;
    private Double longitude;
}
