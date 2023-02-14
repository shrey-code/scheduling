package com.atlassian.example.scheduling;

import twitter4j.Tweet;

import java.util.Date;
import java.util.List;

public interface TwitterMonitor {

    public String getQuery();
    public long getInterval();
    public List<Tweet> getTweets();
    public Date getLastRun();
    public void reschedule(String query, long interval);
}