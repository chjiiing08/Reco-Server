package com.example.reco_test.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class InquiryRequestDTO {

    private UUID userId;

    private String content;
}
