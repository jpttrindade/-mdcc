package com.arctouch.codechallenge.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private final MovieClickListener mClickListener;
    private List<Movie> movies;

    public HomeAdapter(MovieClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setList(List<Movie> results) {
        movies = results;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

        private final TextView titleTextView;
        private final TextView genresTextView;
        private final TextView releaseDateTextView;
        private final ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            genresTextView = itemView.findViewById(R.id.genresTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            itemView.setOnClickListener(view -> {
                Movie movie = (Movie) view.getTag();
                mClickListener.onClick(movie, posterImageView, titleTextView);
            });
        }

        public void bind(Movie movie) {
            itemView.setTag(movie);
            titleTextView.setText(movie.title);

            if (movie.genres != null) {
                genresTextView.setText(TextUtils.join(", ", movie.genres));
            }
            releaseDateTextView.setText(movie.releaseDate);

            String posterPath = movie.posterPath;
            if (TextUtils.isEmpty(posterPath) == false) {
                Glide.with(itemView)
                        .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                        .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(posterImageView);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (movies != null) {
            return movies.size();
        } else {
            return 0;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }
}
