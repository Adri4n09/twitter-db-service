package com.adrian.twitter.dao;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class TweetDaoImpl implements TweetDao {

    private static final Logger logger = LoggerFactory.getLogger(TweetDaoImpl.class);
    private static final String SELECT_ALL_TWEETS = "Select * from `twitter`";
    private static final String SELECT_TWITTER_ID_STR_FROM_TWITTER = "SELECT twitter.idStr from `twitter`";
    private static final String COUNT_TWEETS = "SELECT COUNT(*) AS size FROM `twitter`";
    private static final String SELECT_USER_AND_TEXT = "Select twitter.fromUser as fromUser, " +
            "twitter.text as text from `twitter`";

    private Bucket bucket;

    private Gson mapper;

    @Override
    public void addTweet(Tweet tweet) {
    try {
        bucket.insert(JsonDocument.create(String.valueOf(tweet.getId()),
                JsonObject.fromJson(mapper.toJson(tweet))));
        } catch (DocumentAlreadyExistsException e) {
            logger.error("tweet with id: " + tweet.getId() + " already exists");
        }
    }

    @Override
    public void deleteTweet(Tweet tweet) {
        bucket.remove(String.valueOf(tweet.getId()));
    }

    @Override
    public void updateTweet(Tweet tweet) {
        bucket.upsert(JsonDocument.create(String.valueOf(tweet.getId()),
                JsonObject.fromJson(mapper.toJson(tweet))));
    }

    @Override
    public Tweet getTweet(String id) {
        JsonDocument doc = bucket.get(id);
        if (doc != null)
            return mapper.fromJson(doc.content().toString(), Tweet.class);
        else
            return null;
    }

    @Override
    public List<Tweet> getAllTweets() {
        logger.info("getting all tweets");
        N1qlQueryResult result = bucket.query(N1qlQuery.simple(SELECT_ALL_TWEETS));
        return result.allRows().stream().map(mapQueryResultToTweet()).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllTweetsIds() {
        logger.info("getting all tweets id");

        N1qlQueryResult result = bucket.query(N1qlQuery.simple(SELECT_TWITTER_ID_STR_FROM_TWITTER));

        return result.allRows()
                .parallelStream()
                .map(t -> t.value().getString("idStr"))
                .collect(Collectors.toList());
    }

    @Override
    public Long getNrOfTweets() {
        N1qlQueryResult result = bucket.query(N1qlQuery.simple(COUNT_TWEETS));
        if (result.finalSuccess()) {
            return result.allRows().get(0).value().getLong("size");
        }
        return -1L;
    }

    @Override
    public List<String> getFromUserAndText() {
        N1qlQueryResult result = bucket.query(N1qlQuery.simple(SELECT_USER_AND_TEXT));
        if (result.finalSuccess()) {
            return result.allRows().parallelStream()
                    .map(r -> r.value().toString())
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Function<N1qlQueryRow, Tweet> mapQueryResultToTweet() {
        return a -> mapper.fromJson(a.value().get("twitter").toString(), Tweet.class);
    }

    @Autowired
    @Qualifier("create-tweet-bucket")
    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    @Autowired
    public void setMapper(Gson mapper) {
        this.mapper = mapper;
    }
}
