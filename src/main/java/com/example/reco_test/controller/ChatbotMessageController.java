package com.example.reco_test.controller;

import com.example.reco_test.dto.ChatbotMessageDTO;
import com.example.reco_test.entity.ChatbotMessage;
import com.example.reco_test.entity.User;
import com.example.reco_test.repository.ChatbotMessageRepository;
import com.example.reco_test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot/messages")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatbotMessageController {

    private final ChatbotMessageRepository chatbotMessageRepository;
    private final UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public List<ChatbotMessage> getMessages(@PathVariable UUID userId) {
        return chatbotMessageRepository.findByUserIdOrderByCreatedAtAsc(userId);
    }

    @PostMapping
    public ChatbotMessage saveUserMessage(@RequestBody ChatbotMessageDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        ChatbotMessage message = new ChatbotMessage();
        message.setId(UUID.randomUUID());
        message.setUser(user);
        message.setSender("USER");
        message.setContent(dto.getContent());
        message.setCreatedAt(OffsetDateTime.now());

        return chatbotMessageRepository.save(message);
    }

    @PostMapping("/bot")
    public ChatbotMessage saveBotMessage(@RequestBody ChatbotMessageDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        ChatbotMessage message = new ChatbotMessage();
        message.setId(UUID.randomUUID());
        message.setUser(user);
        message.setSender("BOT");
        message.setContent(dto.getContent());
        message.setCreatedAt(OffsetDateTime.now());

        return chatbotMessageRepository.save(message);
    }
}