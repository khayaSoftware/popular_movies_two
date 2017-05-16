package com.example.android.app.khayapopularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by User on 5/16/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private int mMoviesItems;

    public MovieAdapter(int numberOfMovies){
        mMoviesItems = numberOfMovies;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView gridMovieItem;

        public MovieViewHolder(View itemView){
            super(itemView);

            gridMovieItem = (TextView) itemView.findViewById(R.id.tv_item_movie);
            bind(1);
        }

        void bind(int index){
            gridMovieItem.setText(String.valueOf(index));
        }

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForListItem = R.layout.movie_grid_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmedietly = false;

        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmedietly);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
    }

    @Override
    public int getItemCount() {
        return mMoviesItems;
    }


}
