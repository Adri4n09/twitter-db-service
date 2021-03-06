package com.adrian.twitter.service;

import com.adrian.twitter.dao.TweetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetServiceImpl implements TweetService {

    private TweetDao tweetDao;

    @Override
    public void addTweet(Tweet tweet) {
        tweetDao.addTweet(tweet);
    }

    @Override
    public void deleteTweet(Tweet tweet) {
        tweetDao.deleteTweet(tweet);
    }

    @Override
    public void updateTweet(Tweet tweet) {
        tweetDao.updateTweet(tweet);
    }

    @Override
    @Cacheable("getTweet")
    public Tweet getTweet(String id) {
        return tweetDao.getTweet(id);
    }

    @Override
    @Cacheable("getAllTweets")
    public List<Tweet> getAllTweets() {
        return tweetDao.getAllTweets();
    }

    @Override
    @Cacheable("getAllTweetsId")
    public List<String> getAllTweetsIds() {
        return tweetDao.getAllTweetsIds();
    }

    @Override
    public Long getNrOfTweets() {
        return tweetDao.getNrOfTweets();
    }

    @Override
    @Cacheable("getFromUserAndText")
    public List<String> getFromUserAndText() {
        return tweetDao.getFromUserAndText();
    }

    @Autowired
    public void setTweetDao(TweetDao tweetDao) {
        this.tweetDao = tweetDao;
    }
}
