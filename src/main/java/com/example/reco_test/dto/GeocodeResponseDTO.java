package com.example.reco_test.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeocodeResponseDTO {

    private boolean success;

    private Double latitude;

    private Double longitude;

    private String roadAddress;

    private String message;
}