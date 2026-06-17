package com.example.reco_test.dto;

import com.example.reco_test.entity.User;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    private UUID id;
    private String username;
    private String password;
    private String name;

    public static UserDTO from(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        return dto;
    }
}