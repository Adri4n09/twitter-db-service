package com.adrian.twitter.configuration;

import com.adrian.twitter.utils.MyDateTypeAdapter;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@ComponentScan("com.adrian.twitter")
public class CouchbaseConfiguration {

    @Value("${couchbase.cluster}")
    private String cluster;

    @Value("${couchbase.twitter.bucket}")
    private String twitterBucket;

    @Value("${couchbase.delete-event.bucket}")
    private String deleteEventBucket;

    @Bean
    public Cluster cluster() {
        CouchbaseEnvironment couchbaseEnvironment = DefaultCouchbaseEnvironment.builder()
                .connectTimeout(100000000)
                .queryTimeout(100000000)
                .viewTimeout(100000000)
                .build();
        return CouchbaseCluster.create(couchbaseEnvironment, cluster);
    }

    @Bean
    @Qualifier("create-tweet-bucket")
    public Bucket getTwitterBucket() {
        return cluster().openBucket(twitterBucket);
    }

    @Bean
    @Qualifier("delete-event-bucket")
    public Bucket getDeleteEventBucket() {
        return cluster().openBucket(deleteEventBucket);
    }

    @Bean
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter());
        return gsonBuilder.create();
    }

}
