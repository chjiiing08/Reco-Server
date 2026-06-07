package com.example.reco_test.controller;

import com.example.reco_test.dto.SocialLoginDTO;
import com.example.reco_test.entity.SocialAccount;
import com.example.reco_test.entity.User;
import com.example.reco_test.repository.SocialAccountRepository;
import com.example.reco_test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final SocialAccountRepository socialAccountRepository;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    private final WebClient webClient = WebClient.create();

    @PostMapping("/social")
    public User socialLogin(@RequestBody SocialLoginDTO dto) {

        return socialAccountRepository
                .findByProviderAndProviderUserId(
                        dto.getProvider(),
                        dto.getProviderUserId()
                )
                .map(SocialAccount::getUser)
                .orElseGet(() -> {
                    User user = new User();
                    user.setId(UUID.randomUUID());
                    user.setUsername(dto.getUsername());
                    user.setPasswordHash(null);
                    user.setProfileImageUrl(dto.getProfileImageUrl());
                    user.setIsActive(true);
                    user.setCreatedAt(OffsetDateTime.now());
                    user.setUpdatedAt(OffsetDateTime.now());

                    User savedUser = userRepository.save(user);

                    SocialAccount socialAccount = new SocialAccount();
                    socialAccount.setId(UUID.randomUUID());
                    socialAccount.setUser(savedUser);
                    socialAccount.setProvider(dto.getProvider());
                    socialAccount.setProviderUserId(dto.getProviderUserId());
                    socialAccount.setLinkedAt(OffsetDateTime.now());

                    socialAccountRepository.save(socialAccount);

                    return savedUser;
                });
    }

    @GetMapping("/kakao/callback")
    public User kakaoCallback(@RequestParam String code) {
        Map tokenResponse = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=authorization_code" +
                        "&client_id=" + kakaoClientId +
                        "&redirect_uri=" + kakaoRedirectUri +
                        "&code=" + code)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String accessToken = (String) tokenResponse.get("access_token");

        Map userInfo = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String providerUserId = String.valueOf(userInfo.get("id"));

        Map properties = (Map) userInfo.get("properties");

        String nickname = "카카오사용자";
        String profileImage = "";

        if (properties != null) {
            if (properties.get("nickname") != null) {
                nickname = (String) properties.get("nickname");
            }

            if (properties.get("profile_image") != null) {
                profileImage = (String) properties.get("profile_image");
            }
        }

        SocialLoginDTO dto = new SocialLoginDTO();
        dto.setProvider("KAKAO");
        dto.setProviderUserId(providerUserId);
        dto.setUsername(nickname);
        dto.setProfileImageUrl(profileImage);

        return socialLogin(dto);
    }
}