package com.sp.letspace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.chrisbanes.photoview.PhotoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.sp.letspace.R;
import java.util.List;

public class PhotoPagerAdapter extends RecyclerView.Adapter<PhotoPagerAdapter.PhotoViewHolder> {

    private final Context context;
    private final List<String> photos;

    public PhotoPagerAdapter(Context context, List<String> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PhotoView photoView = (PhotoView) LayoutInflater.from(context)
                .inflate(R.layout.item_fullscreen_photo, parent, false);
        return new PhotoViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String photoUrl = photos.get(position);
        Glide.with(context)
                .load(photoUrl)
                .placeholder(R.drawable.baseline_image_placeholder_24)
                .into(holder.photoView);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        PhotoView photoView;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = (PhotoView) itemView;
        }
    }
}
