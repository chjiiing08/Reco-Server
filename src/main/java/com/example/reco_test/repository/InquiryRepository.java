package com.example.reco_test.repository;

import com.example.reco_test.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository
        extends JpaRepository<Inquiry, Long> {
}
