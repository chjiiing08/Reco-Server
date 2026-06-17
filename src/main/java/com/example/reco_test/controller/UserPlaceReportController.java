package com.example.reco_test.controller;

import com.example.reco_test.dto.UserPlaceReportRequestDTO;
import com.example.reco_test.dto.UserPlaceReportResponseDTO;
import com.example.reco_test.service.UserPlaceReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/place-reports")
@RequiredArgsConstructor
public class UserPlaceReportController {

    private final UserPlaceReportService userPlaceReportService;

    @PostMapping
    public UserPlaceReportResponseDTO createReport(
            @RequestBody UserPlaceReportRequestDTO request
    ) {
        return userPlaceReportService.createReport(request);
    }

    @GetMapping("/approved")
    public List<UserPlaceReportResponseDTO> getApprovedReports(
            @RequestParam String placeType,
            @RequestParam String district
    ) {
        return userPlaceReportService.getApprovedReports(placeType, district);
    }

    @GetMapping("/my")
    public List<UserPlaceReportResponseDTO> getMyReports(
            @RequestParam String userId
    ) {
        return userPlaceReportService.getMyReports(userId);
    }
}