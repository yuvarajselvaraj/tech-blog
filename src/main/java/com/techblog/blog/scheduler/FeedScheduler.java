package com.techblog.blog.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.techblog.blog.service.LaravelNewsFeedService;

@Component
public class FeedScheduler {
    private static final Logger logger = LoggerFactory.getLogger(FeedScheduler.class);

    private final LaravelNewsFeedService feedService;

    public FeedScheduler(LaravelNewsFeedService feedService) {
        this.feedService = feedService;
    }

    // Default: minute 5 of every hour; can be overridden via property `laravel.feed.cron`
    @Scheduled(cron = "${laravel.feed.cron:0 * * * * *}")
    public void pullLaravelNews() {
        int added = feedService.syncLatest();
        int backfilled = feedService.backfillMissingContent();
        if (added > 0) {
            logger.info("Laravel News sync added {} new posts", added);
        }
        if (backfilled > 0) {
            logger.info("Laravel News backfilled content for {} posts", backfilled);
        }
    }
}

