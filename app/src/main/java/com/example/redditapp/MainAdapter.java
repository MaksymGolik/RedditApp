package com.example.redditapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<RedditPost> redditPostList;
    Activity activity;

    public MainAdapter(List<RedditPost> redditPostList, Activity activity){
        this.redditPostList = redditPostList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RedditPost redditPost = redditPostList.get(position);
        Glide.with(activity).load(redditPost.getThumbnailUrl()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.textView.setText(redditPost.getName());
        holder.imageView.setOnClickListener(v -> {
            Uri uri = Uri.parse(redditPost.getFullSizeMediaUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(v.getContext(),intent,intent.getExtras());
        });
    }

    @Override
    public int getItemCount() {
        return redditPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
