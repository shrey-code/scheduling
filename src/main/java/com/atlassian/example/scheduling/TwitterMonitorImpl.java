package com.atlassian.example.scheduling;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.lifecycle.LifecycleAware;
import com.atlassian.sal.api.scheduling.PluginScheduler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import twitter4j.Tweet;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
public class TwitterMonitorImpl implements TwitterMonitor, LifecycleAware {

    /* package */ static final String KEY = TwitterMonitorImpl.class.getName() + ":instance";
    private static final String JOB_NAME = TwitterMonitorImpl.class.getName() + ":job";

    private final Logger logger = Logger.getLogger(TwitterMonitorImpl.class);
    @ComponentImport
    private final PluginScheduler pluginScheduler;  // provided by SAL

    private String query = "Atlassian"; // default Twitter search
    private long interval = 5000L;      // default job interval (5 sec)
    private List<Tweet> tweets;         // results of the last search
    private Date lastRun = null;        // time when the last search returned

    @Autowired
    public TwitterMonitorImpl(PluginScheduler pluginScheduler) {
        this.pluginScheduler = pluginScheduler;
    }

    // declared by LifecycleAware

    public void onStart() {
        reschedule(query, interval);
    }

    public void reschedule(String query, long interval) {
        this.query = query;
        this.interval = interval;

        pluginScheduler.scheduleJob(
                JOB_NAME,                   // unique name of the job
                TwitterQueryTask.class,     // class of the job
                new HashMap<String,Object>() {{
                    put(KEY, TwitterMonitorImpl.this);
                }},                         // data that needs to be passed to the job
                new Date(),                 // the time the job is to start
                interval);                  // interval between repeats, in milliseconds
        logger.info(String.format("Twitter search task scheduled to run every %dms", interval));
    }

    public String getQuery() {
        return query;
    }

    @Override
    public long getInterval() {
        return 0;
    }

    @Override
    public List<Tweet> getTweets() {
        return null;
    }

    @Override
    public Date getLastRun() {
        return null;
    }

    /* package */ void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    /* package */ void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }
}