package com.example.myw;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {

    private List<Uri> mediaUris;

    public ImagePagerAdapter(List<Uri> mediaUris) {
        this.mediaUris = mediaUris;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate a layout containing only an ImageView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_pager, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri uri = mediaUris.get(position);
        Glide.with(holder.itemView.getContext())
                .load(uri)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mediaUris.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivPagerImage);
        }
    }
}