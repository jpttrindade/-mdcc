package com.arctouch.codechallenge.ui.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Genre;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreVH> {
    private List<Genre> mDataset;
    @NonNull
    @Override
    public GenreVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_item, parent, false);
        return new GenreVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreVH holder, int position) {
        Genre genre = mDataset.get(position);
        holder.tvGenre.setText(genre.name);
    }

    @Override
    public int getItemCount() {
        return (mDataset != null) ? mDataset.size() : 0;
    }

    public void setGenres(List<Genre> genreList) {
        mDataset = genreList;
        notifyDataSetChanged();
    }

    class GenreVH extends RecyclerView.ViewHolder {
        public TextView tvGenre;
        public GenreVH(View itemView) {
            super(itemView);
            tvGenre = itemView.findViewById(R.id.tvGenre);
        }
    }
}

