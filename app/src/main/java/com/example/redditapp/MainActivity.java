package com.example.redditapp;

import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<RedditPost> redditPostList = new ArrayList<>();
    MainAdapter mainAdapter;
    
    String after;
    int limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.scrollable);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        mainAdapter = new MainAdapter(redditPostList, MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);

        getRedditPosts(limit, after);

        nestedScrollView.setOnScrollChangeListener(
                (NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(scrollY == v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                after = redditPostList.get(redditPostList.size()-1).getName();
                progressBar.setVisibility(View.VISIBLE);
                getRedditPosts(limit,after);
            }
        });
    }

    private void getRedditPosts(int limit, String after) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.reddit.com/")
                .addConverterFactory(ScalarsConverterFactory.create()).build();

        IMain main = retrofit.create(IMain.class);
        Call<String> call = main.STRING_CALL(limit, after);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        parseRedditJson(jsonObject);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void parseRedditJson(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONObject(getString(R.string.posts))
                .getJSONArray(getString(R.string.posts_array));
        for(int i = 0; i<jsonArray.length(); i++){
            JSONObject post = jsonArray.getJSONObject(i);
            JSONObject postData = post.getJSONObject("data");
            RedditPost redditPost = new RedditPost();
            redditPost.setName(postData.getString("name"));
            redditPost.setTitle(postData.getString("title"));
            redditPost.setThumbnailUrl(postData.getString("thumbnail"));
            if(postData.has("secure_media") && !postData.isNull("secure_media")
                    && postData.getJSONObject("secure_media").has("reddit_video")
                    && !postData.getJSONObject("secure_media").isNull("reddit_video")
                    && postData.getJSONObject("secure_media").getJSONObject("reddit_video").has("fallback_url")
                    && !postData.getJSONObject("secure_media").getJSONObject("reddit_video").isNull("fallback_url")
            ){
                redditPost.setFullSizeMediaUrl(postData.getJSONObject("secure_media")
                        .getJSONObject("reddit_video").getString("fallback_url"));
            } else if(postData.has("url_overridden_by_dest")&&!postData.isNull("url_overridden_by_dest")) {
                redditPost.setFullSizeMediaUrl(postData.getString("url_overridden_by_dest"));
            } else redditPost.setFullSizeMediaUrl(postData.getString("thumbnail"));
            redditPostList.add(redditPost);
        }
        mainAdapter = new MainAdapter(redditPostList, MainActivity.this);
        recyclerView.setAdapter(mainAdapter);
    }
}