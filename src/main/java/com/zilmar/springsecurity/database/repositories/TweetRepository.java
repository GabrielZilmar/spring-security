package com.zilmar.springsecurity.database.repositories;

import com.zilmar.springsecurity.modules.tweet.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, UUID> {

}
