package com.example.reco_test.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPlaceReportRequestDTO {

    private String userId;

    private String placeType;

    private String name;

    private String district;

    private String address;

    private String detailAddress;

    private String description;
}