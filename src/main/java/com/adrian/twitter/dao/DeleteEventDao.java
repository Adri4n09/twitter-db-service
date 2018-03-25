package com.adrian.twitter.dao;

import com.adrian.twitter.model.DeleteEvent;

import java.util.List;

public interface DeleteEventDao {

    void addDeleteEvent(DeleteEvent deleteEvent);

    void updateDeleteEvent(DeleteEvent deleteEvent);

    void deleteDeleteEvent(DeleteEvent deleteEvent);

    DeleteEvent getDeleteEvent(long tweetId);

    List<DeleteEvent> getAllDeleteEvents();

    List<Long> getAllDeleteEventsIds();
}
