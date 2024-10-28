package com.zilmar.springsecurity.modules.tweet;

import com.zilmar.springsecurity.database.repositories.TweetRepository;
import com.zilmar.springsecurity.database.repositories.UserRepository;
import com.zilmar.springsecurity.modules.roles.enums.RolesValues;
import com.zilmar.springsecurity.modules.tweet.dto.CreateTweetDTO;
import com.zilmar.springsecurity.modules.tweet.dto.ListTweetsDTO;
import com.zilmar.springsecurity.modules.tweet.dto.TweetItemDTO;
import com.zilmar.springsecurity.modules.tweet.entities.Tweet;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(
            @PathVariable("id") UUID id,
            JwtAuthenticationToken token
    ) {
        var user = this.userRepository.findById(UUID.fromString(token.getName()));
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User doesn't exists.");
        }

        var tweet = this.tweetRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet was not found."));
        if (!tweet.getUser().getId().equals(user.get().getId())) {
            var isAdmin = user.get().getRoles()
                    .stream()
                    .anyMatch(role -> role.getName().name().equalsIgnoreCase(RolesValues.ADMIN.name()));
            if (!isAdmin) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You only can delete your own tweets.");
            }
        }

        this.tweetRepository.deleteById(tweet.getId());

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ListTweetsDTO> listTweets(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        var tweets = tweetRepository.findAll(
                PageRequest.of(page, pageSize, Sort.Direction.DESC, "createdAt")
        ).map(tweet -> new TweetItemDTO(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser().getUsername()
        ));

        return ResponseEntity.ok(
                new ListTweetsDTO(
                        tweets.getContent(),
                        page,
                        pageSize,
                        tweets.getTotalPages(),
                        tweets.getTotalElements()
                )
        );
    }
}
