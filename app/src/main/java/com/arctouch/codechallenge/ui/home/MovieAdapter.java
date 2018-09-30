package com.arctouch.codechallenge.ui.home;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.arctouch.codechallenge.util.NetworkState;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Objects;

public class MovieAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {
    private final MovieClickListener mMovieClickListner;
    private NetworkState networkState;

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    public MovieAdapter(MovieClickListener movieClickListener) {
        super(DIFF_CALLBACK);
        mMovieClickListner = movieClickListener;
    }

    private static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        // The ID property identifies when items are the same.
        @Override
        public boolean areItemsTheSame(Movie oldItem, Movie newItem) {
            return oldItem.id == newItem.id;
        }

        // Use Object.equals() to know when an item's content changes.
        // Implement equals(), or write custom data comparison logic here.
        @Override
        public boolean areContentsTheSame(Movie oldItem, Movie newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == TYPE_PROGRESS) {
            view = layoutInflater.inflate(R.layout.loading, parent, false);
            return new NetworkStateVH(view);
        } else {
            view = layoutInflater.inflate(R.layout.movie_item, parent, false);
            return new MovieViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            Movie movie = getItem(position);
            ((MovieViewHolder) holder).bindTo(movie);
        } else {
            ((NetworkStateVH) holder).bindTo(networkState);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }


    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;

        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }


    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

        private final TextView titleTextView;
        private final TextView genresTextView;
        private final TextView releaseDateTextView;
        private final ImageView posterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            genresTextView = itemView.findViewById(R.id.genresTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            itemView.setOnClickListener(view -> {
                Movie movie = (Movie) view.getTag();
                mMovieClickListner.onClick(movie);
            });
        }

        public void bindTo(Movie movie) {
            itemView.setTag(movie);
            titleTextView.setText(movie.title);
            if (movie.genres != null) {
                genresTextView.setText(TextUtils.join(", ", movie.genres));
            }
            releaseDateTextView.setText(movie.releaseDate);

            String posterPath = movie.posterPath;
            if (!TextUtils.isEmpty(posterPath)) {
                Glide.with(itemView)
                        .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                        .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(posterImageView);
            }
        }
    }


    class NetworkStateVH extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public NetworkStateVH(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }

        public void bindTo(NetworkState networkState) {
            if (MovieAdapter.this.networkState != null && MovieAdapter.this.networkState == NetworkState.RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }

            if (MovieAdapter.this.networkState != null && MovieAdapter.this.networkState == NetworkState.FAILED) {
//                binding.errorMsg.setVisibility(View.VISIBLE);
//                binding.errorMsg.setText(MovieAdapter.this.networkState.getMsg());
            } else {
//                binding.errorMsg.setVisibility(View.GONE);
            }
        }
    }
}
