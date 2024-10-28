package com.zilmar.springsecurity.modules.session.dto;

public record LoginRequestDTO(
        String username,
        String password
) {
}
