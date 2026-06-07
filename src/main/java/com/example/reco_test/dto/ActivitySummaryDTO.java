package com.example.reco_test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class ActivitySummaryDTO {

    private long totalCount;
    private long todayCount;
    private List<String> todayItems;
    private List<CategoryCountDTO> monthlyStats;

    private long thisMonthCount;
    private long lastMonthCount;
    private long differenceFromLastMonth;
}
