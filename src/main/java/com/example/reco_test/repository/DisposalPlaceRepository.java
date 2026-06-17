package com.example.reco_test.repository;

import com.example.reco_test.entity.DisposalPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DisposalPlaceRepository extends JpaRepository<DisposalPlace, UUID> {

    List<DisposalPlace> findByPlaceType(String placeType);

    List<DisposalPlace> findByDistrict(String district);

    List<DisposalPlace> findByPlaceTypeAndDistrict(String placeType, String district);

    long countByPlaceType(String placeType);

    List<DisposalPlace> findByPlaceTypeAndDistrictContaining(String placeType, String district);

    List<DisposalPlace> findByPlaceTypeAndAddressContaining(String placeType, String keyword);
}