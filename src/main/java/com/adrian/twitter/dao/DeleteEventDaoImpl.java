package com.adrian.twitter.dao;

import com.adrian.twitter.model.DeleteEvent;
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
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class DeleteEventDaoImpl implements DeleteEventDao {

    private static final Logger logger = LoggerFactory.getLogger(DeleteEventDaoImpl.class);
    private static final String SELECT_ALL_DELETE_EVENTS = "Select * from `delete-event`";
    private static final String SELECT_DELETE_EVENT_TWEET_ID_FROM_DELETE_EVENT = "SELECT delete-event.tweetId from `delete-event`";

    private Bucket bucket;

    private Gson mapper;

    @Override
    public void addDeleteEvent(DeleteEvent deleteEvent) {
        try {
            bucket.insert(JsonDocument.create(String.valueOf(deleteEvent.getTweetId()),
                    JsonObject.fromJson(mapper.toJson(deleteEvent))));
        } catch (DocumentAlreadyExistsException e) {
            logger.error("delete event with tweetId: " + deleteEvent.getTweetId() + " already exists");
        }

    }

    @Override
    public void updateDeleteEvent(DeleteEvent deleteEvent) {
        bucket.upsert(JsonDocument.create(String.valueOf(deleteEvent.getTweetId()),
                JsonObject.fromJson(mapper.toJson(deleteEvent))));
        logger.info("deleteEvent update: {}", deleteEvent.toString());
    }

    @Override
    public void deleteDeleteEvent(DeleteEvent deleteEvent) {
        bucket.remove(String.valueOf(deleteEvent.getTweetId()));
        logger.info("deleteEvent with tweetId: {}, deleted.", deleteEvent.getTweetId());
    }

    @Override
    public DeleteEvent getDeleteEvent(long tweetId) {
        return mapper.fromJson(bucket.get(String.valueOf(tweetId)).content().toString(), DeleteEvent.class);
    }

    @Override
    public List<DeleteEvent> getAllDeleteEvents() {
        N1qlQueryResult result = bucket.query(N1qlQuery.simple(SELECT_ALL_DELETE_EVENTS));
        return result.allRows().stream().map(mapQueryResultToDeleteEvent()).collect(Collectors.toList());
    }

    @Override
    public List<Long> getAllDeleteEventsIds() {
        logger.info("getting all delete events ids");

        N1qlQueryResult result = bucket.query(N1qlQuery.simple(SELECT_DELETE_EVENT_TWEET_ID_FROM_DELETE_EVENT));

        return result.allRows().stream().map(t -> Long.getLong(t.value().getString("tweetId"))).collect(Collectors.toList());
    }

    private Function<N1qlQueryRow, DeleteEvent> mapQueryResultToDeleteEvent() {
        return a -> mapper.fromJson(a.value().get(bucket.name()).toString(), DeleteEvent.class);
    }

    @Autowired
    @Qualifier("delete-event-bucket")
    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    @Autowired
    public void setMapper(Gson mapper) {
        this.mapper = mapper;
    }
}
