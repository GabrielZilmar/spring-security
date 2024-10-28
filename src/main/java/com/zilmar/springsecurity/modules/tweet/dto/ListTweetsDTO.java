package com.zilmar.springsecurity.modules.tweet.dto;

import java.util.List;

public record ListTweetsDTO(
        List<TweetItemDTO> tweetItems,
        int page,
        int pageSize,
        int totalPages,
        long totalElements
) {
}
