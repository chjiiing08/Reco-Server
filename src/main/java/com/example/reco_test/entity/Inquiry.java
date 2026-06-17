package com.example.reco_test.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "inquiries")
public class Inquiry{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String status;

    private OffsetDateTime createdAt;
}
