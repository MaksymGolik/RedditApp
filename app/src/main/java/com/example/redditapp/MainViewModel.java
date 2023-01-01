package com.example.redditapp;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    int limit = 15;
    String after;
    List<RedditPost> redditPostList = new ArrayList<>();


    public void addRedditPost(RedditPost redditPost){
        redditPostList.add(redditPost);
    }

}
