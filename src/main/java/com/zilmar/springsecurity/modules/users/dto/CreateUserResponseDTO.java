package com.zilmar.springsecurity.modules.users.dto;

import com.zilmar.springsecurity.modules.users.entities.User;

public record CreateUserResponseDTO(
        User user
) {
}
