package com.example.redditapp;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
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

public class MainActivity extends AppCompatActivity {
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    MainAdapter mainAdapter;
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.scrollable);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainAdapter = new MainAdapter(mainViewModel.getRedditPostList(), MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);

        if(mainViewModel.getRedditPostList().isEmpty()) getRedditPosts(mainViewModel.getLimit(), mainViewModel.getAfter());

        nestedScrollView.setOnScrollChangeListener(
                (NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(scrollY == v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                mainViewModel.setAfter(mainViewModel.getRedditPostList()
                        .get(mainViewModel.getRedditPostList().size()-1).getName());
                progressBar.setVisibility(View.VISIBLE);
                getRedditPosts(mainViewModel.getLimit(), mainViewModel.getAfter());
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
                        Log.e("onResponse", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("onFailure",t.getMessage());
            }
        });
    }

    private void parseRedditJson(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONObject("data")
                .getJSONArray("children");
        for(int i = 0; i<jsonArray.length(); i++){
            JSONObject post = jsonArray.getJSONObject(i);
            JSONObject postData = post.getJSONObject("data");
            RedditPost redditPost = new RedditPost();
            redditPost.setName(postData.getString("name"));
            redditPost.setTitle(postData.getString("title"));
            redditPost.setThumbnailUrl(postData.getString("thumbnail"));
            redditPost.setAuthorName(postData.getString("author"));
            redditPost.setCreatedAt(postData.getLong("created"));
            redditPost.setNumComments(postData.getInt("num_comments"));
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
            mainViewModel.addRedditPost(redditPost);
        }
        mainAdapter = new MainAdapter(mainViewModel.getRedditPostList(), MainActivity.this);
        recyclerView.setAdapter(mainAdapter);
    }
}