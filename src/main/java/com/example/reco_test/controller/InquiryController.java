package com.example.reco_test.controller;

import com.example.reco_test.dto.InquiryRequestDTO;
import com.example.reco_test.entity.Inquiry;
import com.example.reco_test.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
public class InquiryController {

    private final InquiryRepository inquiryRepository;

    @PostMapping
    public ResponseEntity<Void> createInquiry(
            @RequestBody InquiryRequestDTO request
    ) {

        if (request.getContent() == null ||
                request.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Inquiry inquiry = new Inquiry();

        inquiry.setUserId(request.getUserId());
        inquiry.setContent(request.getContent().trim());

        inquiry.setStatus("WAITING");
        inquiry.setCreatedAt(OffsetDateTime.now());

        inquiryRepository.save(inquiry);

        return ResponseEntity.ok().build();
    }
}
