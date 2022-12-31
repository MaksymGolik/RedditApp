package com.example.redditapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

import static androidx.core.content.ContextCompat.startActivity;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<RedditPost> redditPostList;
    Activity activity;
    public static final int REQUEST_CODE = 1;

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
        holder.authorCreated.setText(new StringBuffer(activity.getBaseContext().getString(R.string.concat_author)).append(" ")
                .append(redditPost.getAuthorName()).append(" ").append(redditPost.getHoursAgo()).append(" ")
                .append(activity.getBaseContext().getString(R.string.concat_created)));
        holder.comments.setText(String.valueOf(redditPost.getNumComments()));

        holder.imageView.setOnClickListener(v -> {
            Uri uri = Uri.parse(redditPost.getFullSizeMediaUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(v.getContext(),intent,intent.getExtras());
        });

        holder.save.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                ContentResolver contentResolver = v.getContext().getContentResolver();
                Uri images;
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                } else{
                    images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis()+".jpg");
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*");
                Uri uri = contentResolver.insert(images, contentValues);
                Bitmap bitmap = ((GlideBitmapDrawable)holder.imageView.getDrawable().getCurrent()).getBitmap();

                try(OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri))) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    Objects.requireNonNull(outputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        });
    }

//    private void saveImage(String thumbnailUrl) {
//        Uri uri;
//        ContentResolver contentResolver = context
//    }

    @Override
    public int getItemCount() {
        return redditPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView authorCreated;
        TextView save;
        TextView comments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            authorCreated = itemView.findViewById(R.id.author_created);
            save = itemView.findViewById(R.id.save);
            comments = itemView.findViewById(R.id.comments);
        }


    }
}
