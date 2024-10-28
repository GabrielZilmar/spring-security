package com.zilmar.springsecurity.modules.tweet;

import com.zilmar.springsecurity.database.repositories.TweetRepository;
import com.zilmar.springsecurity.database.repositories.UserRepository;
import com.zilmar.springsecurity.modules.tweet.dto.CreateTweetDTO;
import com.zilmar.springsecurity.modules.tweet.entities.Tweet;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/tweets")
@AllArgsConstructor
public class TweetController {
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @PostMapping
    public ResponseEntity<Tweet> createTweet(
            @RequestBody CreateTweetDTO body,
            JwtAuthenticationToken token
    ) {
        var user = this.userRepository.findById(UUID.fromString(token.getName()));
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User doesn't exists.");
        }

        Tweet tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(body.content());
        this.tweetRepository.save(tweet);

        return ResponseEntity.ok(tweet);
    }
}
