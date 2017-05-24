package com.example.android.app.khayapopularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by User on 5/16/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private ArrayList<Movie> mMovies;
    static final String TAG = MovieAdapter.class.getSimpleName();
    private int mMoviesItems;
    private  Context context;
    public MovieAdapter(int numberOfMovies, Context context){

        mMoviesItems = numberOfMovies;
        this.context = context;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{


        ImageView gridMovieImage;

        public MovieViewHolder(View itemView){
            super(itemView);

            gridMovieImage = (ImageView) itemView.findViewById(R.id.movie_image);

        }

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //context = parent.getContext();
        int layoutForListItem = R.layout.movie_grid_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmedietly = false;

        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmedietly);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String imagePath = "http://image.tmdb.org/t/p/w185/" + mMovies.get(position).posterPath;
        Log.v(TAG, "Image url - " + imagePath);
        try{
            Log.v(TAG, "Image url - " + imagePath);
            Picasso.with(context).load(imagePath).into(holder.gridMovieImage);
        }catch (Exception e){
            Log.d(TAG, "error");
            Log.v(TAG, "Image url - " + imagePath);
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if(null == mMovies)return 0;
        return mMovies.size();
    }

    public void setMovieData(ArrayList<Movie> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }
}
