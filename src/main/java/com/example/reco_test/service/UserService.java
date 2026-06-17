package com.example.reco_test.service;

import com.example.reco_test.dto.UserDTO;
import com.example.reco_test.dto.UserUpdateRequestDTO;
import com.example.reco_test.entity.User;
import com.example.reco_test.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO getUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return UserDTO.from(user);
    }

    @Transactional
    public UserDTO updateUserName(UUID userId, UserUpdateRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }

        user.setName(request.getName().trim());
        user.setUpdatedAt(OffsetDateTime.now());

        return UserDTO.from(user);
    }
}