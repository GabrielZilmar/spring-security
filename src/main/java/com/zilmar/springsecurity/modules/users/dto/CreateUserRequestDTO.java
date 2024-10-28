package com.zilmar.springsecurity.modules.users.dto;

public record CreateUserRequestDTO(
        String username,
        String password
) {
}
