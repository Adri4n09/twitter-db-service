package com.adrian.twitter.controllers;

import com.adrian.twitter.operations.DbOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
