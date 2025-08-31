package com.techblog.blog.service;

import com.techblog.blog.model.BlogPost;
import com.techblog.blog.repository.BlogPostRepository;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LaravelNewsFeedService {

    private static final String FEED_URL = "https://feed.laravel-news.com/";

    private final BlogPostRepository blogPostRepository;

    public LaravelNewsFeedService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Transactional
    public int syncLatest() {
        int numAdded = 0;
        try (XmlReader reader = new XmlReader(new URL(FEED_URL))) {
            SyndFeed feed = new SyndFeedInput().build(reader);
            if (feed.getEntries().size() == 0) {
                return 0;
            }
            for (SyndEntry entry : feed.getEntries()) {
                String guid = entry.getUri() != null ? entry.getUri() : entry.getLink();
                if (guid == null || guid.isBlank()) {
                    continue;
                }
                if (blogPostRepository.existsByGuid(guid)) {
                    continue;
                }
                BlogPost post = new BlogPost();
                post.setGuid(guid);
                post.setTitle(entry.getTitle());
                post.setLink(entry.getLink());
                post.setPublishedAt(convertDate(entry.getPublishedDate()));
                post.setContent(extractContent(entry));
                blogPostRepository.save(post);
                numAdded++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to sync Laravel News feed", e);
        }
        return numAdded;
    }

    // Optional: backfill content for posts with null content on subsequent runs
    @Transactional
    public int backfillMissingContent() {
        int numUpdated = 0;
        try (XmlReader reader = new XmlReader(new URL(FEED_URL))) {
            SyndFeed feed = new SyndFeedInput().build(reader);
            for (SyndEntry entry : feed.getEntries()) {
                String guid = entry.getUri() != null ? entry.getUri() : entry.getLink();
                if (guid == null || guid.isBlank()) {
                    continue;
                }
                // Load by GUID when content is missing
                BlogPost existing = blogPostRepository.findAll().stream()
                    .filter(p -> guid.equals(p.getGuid()) && (p.getContent() == null || p.getContent().isBlank()))
                    .findFirst()
                    .orElse(null);
                if (existing != null) {
                    existing.setContent(extractContent(entry));
                    blogPostRepository.save(existing);
                    numUpdated++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to backfill Laravel News content", e);
        }
        return numUpdated;
    }

    private static String extractContent(SyndEntry entry) {
        // Prefer content:encoded if present, fallback to description
        if (entry.getContents() != null && !entry.getContents().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (SyndContent c : entry.getContents()) {
                if (c != null && c.getValue() != null) {
                    sb.append(c.getValue());
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        }
        return entry.getDescription() != null ? entry.getDescription().getValue() : null;
    }

    private static OffsetDateTime convertDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.of("UTC")).toOffsetDateTime();
    }
}

