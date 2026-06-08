package com.example.reco_test.config;

import com.example.reco_test.entity.DisposalPlace;
import com.example.reco_test.repository.DisposalPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvDataLoader implements CommandLineRunner {

    private final DisposalPlaceRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) {
            System.out.println("disposal_places 데이터가 이미 있어 CSV 적재를 건너뜁니다.");
            return;
        }

        loadCsv("data/battery.csv");
        loadCsv("data/clothes.csv");
        loadCsv("data/trash.csv");
        loadCsv("data/recycle.csv");
    }

    private void loadCsv(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);

        try (
                Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                CSVParser parser = CSVFormat.DEFAULT
                        .builder()
                        .setHeader("name", "category", "district", "address", "latitude", "longitude")
                        .setSkipHeaderRecord(true)
                        .build()
                        .parse(reader)
        ) {
            List<DisposalPlace> places = new ArrayList<>();

            for (CSVRecord record : parser) {
                String lat = record.get("latitude");
                String lng = record.get("longitude");

                if (!isValidNumber(lat) || !isValidNumber(lng)) {
                    continue;
                }


                places.add(DisposalPlace.builder()
                        .name(record.get("name"))
                        .placeType(record.get("category"))
                        .district(record.get("district"))
                        .address(record.get("address"))
                        .latitude(Double.parseDouble(lat))
                        .longitude(Double.parseDouble(lng))
                        .updatedAt(LocalDateTime.now())
                        .build());
            }


            repository.saveAll(places);
            System.out.println(path + " 적재 완료: " + places.size() + "개");
        }
    }
    private boolean isValidNumber(String value) {
        if (value == null) return false;

        String v = value.trim();

        if (v.isBlank()) return false;
        if (v.equals("_")) return false;
        if (v.equals("-")) return false;

        try {
            Double.parseDouble(v);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}