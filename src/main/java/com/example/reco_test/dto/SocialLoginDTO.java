package com.example.reco_test.dto;

import lombok.Data;

@Data
public class SocialLoginDTO {

    private String provider;
    private String providerUserId;
    private String username;
    private String profileImageUrl;
}