package com.example.reco_test.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "disposal_places")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisposalPlace {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(name = "place_type")
    private String placeType;

    private Double latitude;

    private Double longitude;

    private String address;

    private String district;

    private String phone;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}