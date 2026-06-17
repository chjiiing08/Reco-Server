package com.example.reco_test.service;

import com.example.reco_test.dto.UserPlaceReportRequestDTO;
import com.example.reco_test.dto.UserPlaceReportResponseDTO;
import com.example.reco_test.entity.UserPlaceReport;
import com.example.reco_test.repository.UserPlaceReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.reco_test.dto.GeocodeRequestDTO;
import com.example.reco_test.dto.GeocodeResponseDTO;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPlaceReportService {

    private final UserPlaceReportRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    public UserPlaceReportResponseDTO createReport(UserPlaceReportRequestDTO request) {

        String detailAddress = request.getDetailAddress() == null
                ? ""
                : request.getDetailAddress();

        boolean alreadyReported =
                repository.existsByUserIdAndPlaceTypeAndDistrictAndAddressAndDetailAddress(
                        request.getUserId(),
                        request.getPlaceType(),
                        request.getDistrict(),
                        request.getAddress(),
                        detailAddress
                );

        if (alreadyReported) {
            throw new IllegalArgumentException("이미 제보한 위치입니다.");
        }

        UserPlaceReport report = UserPlaceReport.builder()
                .userId(request.getUserId())
                .placeType(request.getPlaceType())
                .name(request.getName())
                .district(request.getDistrict())
                .address(request.getAddress())
                .detailAddress(detailAddress)
                .description(request.getDescription())
                .status("PENDING")
                .geocodeStatus("WAITING")
                .build();

        UserPlaceReport saved = repository.save(report);

        long reportCount =
                repository.countDistinctUserIdByPlaceTypeAndDistrictAndAddressAndDetailAddress(
                        request.getPlaceType(),
                        request.getDistrict(),
                        request.getAddress(),
                        detailAddress
                );

        if (reportCount >= 3) {
            requestGeocodeAndApprove(
                    request.getPlaceType(),
                    request.getDistrict(),
                    request.getAddress(),
                    detailAddress
            );
        }

        return toResponseDTO(saved);
    }

    private void requestGeocodeAndApprove(
            String placeType,
            String district,
            String address,
            String detailAddress
    ) {
        List<UserPlaceReport> reports =
                repository.findByPlaceTypeAndDistrictAndAddressAndDetailAddress(
                        placeType,
                        district,
                        address,
                        detailAddress
                );

        for (UserPlaceReport report : reports) {
            report.setGeocodeStatus("REQUESTED");
        }
        repository.saveAll(reports);

        try {
            GeocodeRequestDTO geocodeRequest = GeocodeRequestDTO.builder()
                    .address(address)
                    .district(district)
                    .placeType(placeType)
                    .build();

            GeocodeResponseDTO response = restTemplate.postForObject(
                    "http://localhost:3000/api/v1/geocode",
                    geocodeRequest,
                    GeocodeResponseDTO.class
            );

            if (response != null && response.isSuccess()) {
                for (UserPlaceReport report : reports) {
                    report.setLatitude(response.getLatitude());
                    report.setLongitude(response.getLongitude());
                    report.setStatus("APPROVED");
                    report.setGeocodeStatus("SUCCESS");
                }
            } else {
                for (UserPlaceReport report : reports) {
                    report.setGeocodeStatus("FAILED");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            for (UserPlaceReport report : reports) {
                report.setGeocodeStatus("FAILED");
            }
        }

        repository.saveAll(reports);
    }

    public List<UserPlaceReportResponseDTO> getApprovedReports(
            String placeType,
            String district
    ) {
        return repository.findByStatusAndPlaceTypeAndDistrict(
                        "APPROVED",
                        placeType,
                        district
                )
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<UserPlaceReportResponseDTO> getMyReports(String userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private UserPlaceReportResponseDTO toResponseDTO(UserPlaceReport report) {
        return UserPlaceReportResponseDTO.builder()
                .id(report.getId())
                .userId(report.getUserId())
                .placeType(report.getPlaceType())
                .name(report.getName())
                .district(report.getDistrict())
                .address(report.getAddress())
                .detailAddress(report.getDetailAddress())
                .latitude(report.getLatitude())
                .longitude(report.getLongitude())
                .description(report.getDescription())
                .status(report.getStatus())
                .geocodeStatus(report.getGeocodeStatus())
                .createdAt(report.getCreatedAt())
                .build();
    }
}