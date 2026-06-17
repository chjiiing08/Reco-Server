package com.example.reco_test.controller;

import com.example.reco_test.dto.UserDTO;
import com.example.reco_test.dto.UserUpdateRequestDTO;
import com.example.reco_test.entity.User;
import com.example.reco_test.repository.UserRepository;
import com.example.reco_test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/signup")
    public User signup(@RequestBody UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(dto.getUsername());
        user.setPasswordHash(dto.getPassword());

        user.setIsActive(true);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        return userRepository.save(user);
    }
    @PostMapping("/login")
    public User login(@RequestBody UserDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        if (!user.getPasswordHash().equals(dto.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUserName(
            @PathVariable UUID userId,
            @RequestBody UserUpdateRequestDTO request
    ) {
        return ResponseEntity.ok(userService.updateUserName(userId, request));
    }
}