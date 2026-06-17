package com.example.reco_test.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GeocodeRequestDTO {

    private String address;

    private String district;

    private String placeType;
}