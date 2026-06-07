package com.example.reco_test.controller;

import com.example.reco_test.dto.ActivitySummaryDTO;
import com.example.reco_test.dto.AnalysisDTO;
import com.example.reco_test.dto.AnalysisResultDTO;
import com.example.reco_test.dto.CategoryCountDTO;
import com.example.reco_test.entity.AnalysisRecord;
import com.example.reco_test.entity.User;
import com.example.reco_test.repository.AnalysisRecordRepository;
import com.example.reco_test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisRecordRepository analysisRecordRepository;
    private final UserRepository userRepository;

    @PostMapping
    public AnalysisResultDTO saveAnalysis(
            @RequestBody AnalysisDTO dto
    ) {
        AnalysisRecord record = new AnalysisRecord();

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        record.setUser(user);
        record.setId(UUID.randomUUID());
        record.setImageUrl(dto.getImageUrl());
        record.setItemName(dto.getItemName());
        record.setIsRecyclable(dto.getIsRecyclable());
        record.setDisposalMethodSummary(dto.getDisposalMethodSummary());
        record.setAnalyzedAt(LocalDateTime.now());

        AnalysisRecord savedRecord = analysisRecordRepository.save(record);

        String resultType;

        if (dto.getConfidence() == null) {
            resultType = "FAILED";
        } else if (dto.getConfidence() < 70) {
            resultType = "AMBIGUOUS";
        } else if (Boolean.FALSE.equals(dto.getIsRecyclable())) {
            resultType = "NOT_RECYCLABLE";
        } else {
            resultType = "SUCCESS";
        }

        return AnalysisResultDTO.builder()
                .analysisId(savedRecord.getId())
                .resultType(resultType)
                .message("분석 결과가 저장되었습니다.")
                .itemName(savedRecord.getItemName())
                .isRecyclable(savedRecord.getIsRecyclable())
                .disposalMethodSummary(savedRecord.getDisposalMethodSummary())
                .build();
    }

    @GetMapping("/list/{userId}")
    public List<AnalysisRecord> getAnalysisList(@PathVariable UUID userId) {
        return analysisRecordRepository.findByUserIdOrderByAnalyzedAtDesc(userId);
    }

    @GetMapping("/summary/{userId}")
    public ActivitySummaryDTO getSummary(@PathVariable UUID userId){

        long totalCount = analysisRecordRepository.countByUserId(userId);

        LocalDateTime startOfToday =
                LocalDateTime.now().toLocalDate().atStartOfDay();

        long todayCount =
                analysisRecordRepository.countByUserIdAndAnalyzedAtAfter(userId, startOfToday);

        List<String> todayItems =
                analysisRecordRepository.findByUserIdAndAnalyzedAtAfter(userId, startOfToday)
                        .stream()
                        .map(AnalysisRecord::getItemName)
                        .toList();

        LocalDateTime startOfMonth =
                LocalDateTime.now()
                        .withDayOfMonth(1)
                        .toLocalDate()
                        .atStartOfDay();

        List<AnalysisRecord> monthRecords =
                analysisRecordRepository.findByUserIdAndAnalyzedAtAfter(userId, startOfMonth);

        Map<String, Long> monthlyCountMap =
                monthRecords.stream()
                        .collect(Collectors.groupingBy(
                                record -> guessCategoryName(record.getItemName()),
                                Collectors.counting()
                        ));

        List<CategoryCountDTO> monthlyStats = List.of(
                new CategoryCountDTO("플라스틱", monthlyCountMap.getOrDefault("플라스틱", 0L)),
                new CategoryCountDTO("유리", monthlyCountMap.getOrDefault("유리", 0L)),
                new CategoryCountDTO("종이", monthlyCountMap.getOrDefault("종이", 0L)),
                new CategoryCountDTO("캔", monthlyCountMap.getOrDefault("캔", 0L)),
                new CategoryCountDTO("일반", monthlyCountMap.getOrDefault("일반", 0L))
        );
        LocalDateTime startOfThisMonth =
                LocalDateTime.now()
                        .withDayOfMonth(1)
                        .toLocalDate()
                        .atStartOfDay();

        LocalDateTime startOfNextMonth =
                startOfThisMonth.plusMonths(1);

        LocalDateTime startOfLastMonth =
                startOfThisMonth.minusMonths(1);

        long thisMonthCount =
                analysisRecordRepository.countByUserIdAndAnalyzedAtBetween(
                        userId,
                        startOfThisMonth,
                        startOfNextMonth
                );

        long lastMonthCount =
                analysisRecordRepository.countByUserIdAndAnalyzedAtBetween(
                        userId,
                        startOfLastMonth,
                        startOfThisMonth
                );

        long difference =
                thisMonthCount - lastMonthCount;

        return new ActivitySummaryDTO(
                totalCount,
                todayCount,
                todayItems,
                monthlyStats,
                thisMonthCount,
                lastMonthCount,
                difference
        );
    }

    private String guessCategoryName(String itemName) {
        if (itemName == null) return "일반";

        if (itemName.contains("페트") || itemName.contains("플라스틱")) {
            return "플라스틱";
        }
        if (itemName.contains("유리") || itemName.contains("병")) {
            return "유리";
        }
        if (itemName.contains("종이") || itemName.contains("박스") || itemName.contains("컵")) {
            return "종이";
        }
        if (itemName.contains("캔") || itemName.contains("알루미늄")) {
            return "캔";
        }

        return "일반";
    }
}