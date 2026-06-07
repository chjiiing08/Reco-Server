package com.example.reco_test.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "social_accounts")
public class SocialAccount {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String provider; // GOOGLE, KAKAO

    @Column(name = "provider_user_id")
    private String providerUserId;

    @Column(name = "linked_at")
    private OffsetDateTime linkedAt;
}