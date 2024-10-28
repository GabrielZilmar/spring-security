package com.zilmar.springsecurity.modules.session.dto;

public record LoginResponseDTO(
        String accessToken,
        Long expiresIn
) {
}
