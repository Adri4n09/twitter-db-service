package com.adrian.twitter.controllers;

import com.adrian.twitter.operations.DbOperations;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TweetsController {

    @Autowired
    private DbOperations dbOperations;

    @RequestMapping("/getAllTweetsIds")
    public ResponseEntity<List<String>> getAllTweetsIds() {
        return new ResponseEntity<>(dbOperations.getTweetsIds(), HttpStatus.OK);
    }

    @RequestMapping("/getAllTweets")
    public ResponseEntity<List<Tweet>> getAllTweets() {
        return new ResponseEntity<>(dbOperations.getAllTweets(), HttpStatus.OK);
    }

    @RequestMapping("/getAllText")
    public ResponseEntity<List<String>> getAllText() {
        return new ResponseEntity<>(dbOperations.getAllText(), HttpStatus.OK);
    }

    @RequestMapping("/getNumberOfTweetsPerUser")
    public ResponseEntity<Map<String, Integer>> getNumberOfTweetsPerUser() {
        return new ResponseEntity<>(dbOperations.getNumberOfTweetsPerUser(), HttpStatus.OK);
    }

    @RequestMapping("/getDeletedTweetsText")
    public ResponseEntity<List<String>> getDeletedTweetsText() {
        return new ResponseEntity<>(dbOperations.getDeletedTweetsText(), HttpStatus.OK);
    }

    @RequestMapping("/getNrOfTweets")
    public ResponseEntity<Long> getNrOfTweets() {
        return new ResponseEntity<>(dbOperations.getNrOfTweets(), HttpStatus.OK);
    }

    @RequestMapping("getUserAndText")
    public ResponseEntity<List<String>> getUserAndText() {
        return new ResponseEntity<>(dbOperations.getUserAndText(), HttpStatus.OK);
    }
}
