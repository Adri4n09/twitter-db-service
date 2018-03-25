package com.adrian.twitter.operations;

import com.adrian.twitter.service.DeleteEventService;
import com.adrian.twitter.service.TweetService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class DbOperations {

    @Autowired
    private TweetService tweetService;

    @Autowired
    private DeleteEventService deleteEventService;


    public List<String> getTweetsIds() {
        return tweetService.getAllTweetsIds();
    }

    public List<Tweet> getAllTweets() {
        return tweetService.getAllTweets();
    }

}
