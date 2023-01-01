package com.example.redditapp;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private int limit = 15;
    private String after;
    private List<RedditPost> redditPostList = new ArrayList<>();

    public void addRedditPost(RedditPost redditPost){
        redditPostList.add(redditPost);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public List<RedditPost> getRedditPostList() {
        return redditPostList;
    }

    public void setRedditPostList(List<RedditPost> redditPostList) {
        this.redditPostList = redditPostList;
    }
}