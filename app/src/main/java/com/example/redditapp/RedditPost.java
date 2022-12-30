package com.example.redditapp;

public class RedditPost {
    private String name;
    private String title;
    private String thumbnailUrl;
    private String fullSizeMediaUrl;

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
