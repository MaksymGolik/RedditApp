package com.example.redditapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IMain {
    @GET("top.json")
    Call<String> STRING_CALL(@Query("limit") int limit, @Query("after") String after);
}
