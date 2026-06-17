package com.example.reco_test.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

    @Entity
    @Table(name = "user_place_report")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class UserPlaceReport {

        @Id
        @GeneratedValue
        private UUID id;

        @Column(name = "user_id", nullable = false)
        private String userId;

        @Column(name = "place_type", nullable = false)
        private String placeType;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String district;

        @Column(nullable = false)
        private String address;

        @Column(name = "detail_address")
        private String detailAddress;

        private Double latitude;

        private Double longitude;

        @Column(columnDefinition = "TEXT")
        private String description;

        @Column(nullable = false)
        private String status;

        @Column(name = "geocode_status", nullable = false)
        private String geocodeStatus;

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt;

        @PrePersist
        public void prePersist() {
            if (status == null) {
                status = "PENDING";
            }

            if (geocodeStatus == null) {
                geocodeStatus = "WAITING";
            }

            if (createdAt == null) {
                createdAt = LocalDateTime.now();
            }
        }
    }