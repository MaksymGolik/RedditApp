package com.example.redditapp;

import java.time.LocalDateTime;
import java.util.Date;

public class RedditPost {
    private String name;
    private String title;
    private String thumbnailUrl;
    private String fullSizeMediaUrl;
    private String authorName;
    private long createdAt;
    private int numComments;

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public long getCreatedAt() {
        return createdAt;
    }
    public int getHoursAgo(){
        System.out.println(new Date().getTime()+" "+createdAt);
        return (int)(new Date().getTime()-createdAt*1000)/(60*60*1000);
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getFullSizeMediaUrl() {
        return fullSizeMediaUrl;
    }

    public void setFullSizeMediaUrl(String fullSizeMediaUrl) {
        this.fullSizeMediaUrl = fullSizeMediaUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
