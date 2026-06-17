package com.example.reco_test.service;

import com.example.reco_test.dto.DisposalPlaceResponseDTO;
import com.example.reco_test.entity.DisposalPlace;
import com.example.reco_test.entity.UserPlaceReport;
import com.example.reco_test.repository.DisposalPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.reco_test.repository.UserPlaceReportRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DisposalPlaceService {

    private final DisposalPlaceRepository repository;
    private final DistrictResolver districtResolver;
    private final UserPlaceReportRepository userPlaceReportRepository;

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

    public List<DisposalPlaceResponseDTO> getNearbyPlaces(
            String type,
            double latitude,
            double longitude,
            String district
    ) {
        String resolvedDistrict = normalizeDistrict(district);

        if (resolvedDistrict.isBlank()) {
            resolvedDistrict = districtResolver.resolve(latitude, longitude);
        }

        if (resolvedDistrict == null || resolvedDistrict.isBlank()) {
            resolvedDistrict = "관악구";
        }

        List<DisposalPlace> districtPlaces =
                repository.findByPlaceTypeAndDistrictContaining(type, resolvedDistrict);

        List<DisposalPlace> addressPlaces =
                repository.findByPlaceTypeAndAddressContaining(type, resolvedDistrict);

        List<UserPlaceReport> approvedReports =
                userPlaceReportRepository.findByStatusAndPlaceTypeAndDistrict(
                        "APPROVED",
                        type,
                        resolvedDistrict
                );


        List<DisposalPlace> places = Stream.concat(districtPlaces.stream(), addressPlaces.stream())
                .distinct()
                .toList();

        List<DisposalPlaceResponseDTO> result = places.stream()
                .map(place -> DisposalPlaceResponseDTO.builder()
                        .id(place.getId())
                        .name(place.getName())
                        .placeType(place.getPlaceType())
                        .district(place.getDistrict())
                        .address(place.getAddress())
                        .latitude(place.getLatitude())
                        .longitude(place.getLongitude())
                        .build())
                .toList();

        List<DisposalPlaceResponseDTO> reportResult = approvedReports.stream()
                .filter(report -> report.getLatitude() != null && report.getLongitude() != null)
                .collect(java.util.stream.Collectors.toMap(
                        report -> report.getPlaceType()
                                + "|" + report.getDistrict()
                                + "|" + report.getAddress()
                                + "|" + report.getDetailAddress(),
                        report -> report,
                        (first, second) -> first
                ))
                .values()
                .stream()
                .map(report -> DisposalPlaceResponseDTO.builder()
                        .id(report.getId())
                        .name(report.getName())
                        .placeType(report.getPlaceType())
                        .district(report.getDistrict())
                        .address(report.getAddress())
                        .latitude(report.getLatitude())
                        .longitude(report.getLongitude())
                        .build())
                .toList();

        return Stream.concat(result.stream(), reportResult.stream())
                .toList();
    }

    private String normalizeDistrict(String district) {
        if (district == null) return "";

        Matcher matcher = Pattern.compile("[가-힣]+구").matcher(district);

        if (matcher.find()) {
            return matcher.group();
        }

        return "";
    }
}