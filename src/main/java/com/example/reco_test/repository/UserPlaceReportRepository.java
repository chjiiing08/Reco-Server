package com.example.reco_test.repository;

import com.example.reco_test.entity.UserPlaceReport;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;

public interface UserPlaceReportRepository extends JpaRepository<UserPlaceReport, UUID> {

    boolean existsByUserIdAndPlaceTypeAndDistrictAndAddressAndDetailAddress(
            String userId,
            String placeType,
            String district,
            String address,
            String detailAddress
    );

    long countDistinctUserIdByPlaceTypeAndDistrictAndAddressAndDetailAddress(
            String placeType,
            String district,
            String address,
            String detailAddress
    );

    List<UserPlaceReport> findByPlaceTypeAndDistrictAndAddressAndDetailAddress(
            String placeType,
            String district,
            String address,
            String detailAddress
    );

    List<UserPlaceReport> findByStatusAndPlaceTypeAndDistrict(
            String status,
            String placeType,
            String district
    );

    List<UserPlaceReport> findByUserIdOrderByCreatedAtDesc(String userId);
}