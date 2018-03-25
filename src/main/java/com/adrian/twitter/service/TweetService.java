package com.adrian.twitter.service;

import org.springframework.social.twitter.api.Tweet;

import java.util.List;

public interface TweetService {

    void addTweet(Tweet tweet);

    void deleteTweet(Tweet tweet);

    void updateTweet(Tweet tweet);

    Tweet getTweet(String id);

    List<Tweet> getAllTweets();

    List<String> getAllTweetsIds();
}
