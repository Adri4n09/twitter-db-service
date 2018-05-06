package com.adrian.twitter.operations;

import com.adrian.twitter.service.DeleteEventService;
import com.adrian.twitter.service.TweetService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<String> getAllText() {
        return tweetService.getAllTweetsIds()
                .parallelStream()
                .map(t -> tweetService.getTweet(t).getText())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getNumberOfTweetsPerUser() {
        Map<String, Integer> map = new LinkedHashMap<>();
        tweetService.getAllTweetsIds()
                .parallelStream()
                .map(tId -> tweetService.getTweet(tId).getUser().getName())
                .forEach(name -> map.put(name, incrementNrUser(map, name)));
        return map.entrySet()
                .parallelStream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Integer incrementNrUser(Map<String, Integer> map, String name) {
        if (map.keySet().contains(name)) return map.get(name) + 1;
        else return 1;
    }

    public List<String> getDeletedTweetsText() {
        return deleteEventService
                .getAllDeleteEvents()
                .parallelStream()
                .map(d -> tweetService.getTweet(String.valueOf(d.getTweetId())))
                .filter(Objects::nonNull)
                .map(t -> "name: " + t.getUser().getName() + " text: " + t.getText())
                .sorted()
                .collect(Collectors.toList());
    }

    public Long getNrOfTweets() {
        return tweetService.getNrOfTweets();
    }

    public List<String> getUserAndText() {
        return tweetService.getFromUserAndText();
    }

    public Tweet getTweetById(String id) {
        return tweetService.getTweet(id);
    }
}
