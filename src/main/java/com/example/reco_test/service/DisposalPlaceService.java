package com.example.reco_test.service;

import com.example.reco_test.entity.DisposalPlace;
import com.example.reco_test.repository.DisposalPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisposalPlaceService {

    private final DisposalPlaceRepository repository;

    public List<DisposalPlace> getPlaces(String type, String district) {

        if (type != null && district != null) {
            return repository.findByPlaceTypeAndDistrict(type, district);
        }

        if (type != null) {
            return repository.findByPlaceType(type);
        }

        if (district != null) {
            return repository.findByDistrict(district);
        }

        return repository.findAll();
    }
}