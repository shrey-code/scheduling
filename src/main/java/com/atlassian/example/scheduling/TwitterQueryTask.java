package com.atlassian.example.scheduling;

import com.atlassian.sal.api.scheduling.PluginJob;
import org.apache.log4j.Logger;
import twitter4j.Query;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.Date;
import java.util.Map;

public class TwitterQueryTask implements PluginJob {

    private final Logger logger = Logger.getLogger(TwitterQueryTask.class);

    /**
     * Executes this job.
     *
     * @param jobDataMap any data the job needs to execute. Changes to this data will be remembered between executions.
     */
    public void execute(Map<String, Object> jobDataMap) {

        final TwitterMonitorImpl monitor = (TwitterMonitorImpl)jobDataMap.get(TwitterMonitorImpl.KEY);
        assert monitor != null;
        try {
            final Twitter twitter = new Twitter();
            monitor.setTweets(twitter.search(new Query(monitor.getQuery())).getTweets());
            monitor.setLastRun(new Date());
        } catch (TwitterException te) {
            logger.error("Error talking to Twitter: " + te.getMessage(), te);
        }
    }
}