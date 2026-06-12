package com.example.reco_test.controller;

import com.example.reco_test.entity.DisposalPlace;
import com.example.reco_test.service.DisposalPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.reco_test.dto.DisposalPlaceResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class DisposalPlaceController {

    private final DisposalPlaceService disposalPlaceService;

    @GetMapping
    public List<DisposalPlace> getPlaces(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String district
    ) {
        return disposalPlaceService.getPlaces(type, district);
    }

    @GetMapping("/nearby")
    public List<DisposalPlaceResponseDTO> getNearbyPlaces(
            @RequestParam String type,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false) String district
    ) {
        return disposalPlaceService.getNearbyPlaces(type, latitude, longitude, district);
    }
}
