package com.atlassian.example.scheduling;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import twitter4j.Tweet;

import java.util.Date;
import java.util.List;

public class SchedulerAction extends JiraWebActionSupport {

    private final TwitterMonitor twitterMonitor;
    private String query;
    private long interval;

    public SchedulerAction(TwitterMonitor twitterMonitor) {
        this.twitterMonitor = twitterMonitor;
        this.query = twitterMonitor.getQuery();
        this.interval = twitterMonitor.getInterval();
    }

    @Override
    protected String doExecute() throws Exception {
        return SUCCESS;
    }

    public String doReschedule() {
        twitterMonitor.reschedule(query, interval);
        return getRedirect("TwitterScheduler!default.jspa");
    }

    public List<Tweet> getTweets() {
        return twitterMonitor.getTweets();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public Date getLastRun() {
        return twitterMonitor.getLastRun();
    }
}