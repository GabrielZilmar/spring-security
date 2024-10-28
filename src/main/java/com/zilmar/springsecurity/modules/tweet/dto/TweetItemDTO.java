package com.zilmar.springsecurity.modules.tweet.dto;

import java.util.UUID;

public record TweetItemDTO(
        UUID id,
        String content,
        String username
) {
}
