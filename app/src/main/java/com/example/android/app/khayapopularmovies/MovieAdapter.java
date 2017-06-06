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

    public final MovieAdapterOnClickHandler mClickHandler;
    private ArrayList<Movie> mMovies;
    static final String TAG = MovieAdapter.class.getSimpleName();
    private int mMoviesItems;
    private  Context context;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler, Context context){
        this.mClickHandler = clickHandler;
        mMoviesItems = getItemCount();
        this.context = context;
    }

    public interface MovieAdapterOnClickHandler{
        void onClick(Movie movie);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int adapterPostition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPostition);
            mClickHandler.onClick(movie);
        }

        ImageView gridMovieImage;

        public MovieViewHolder(View itemView){
            super(itemView);
            gridMovieImage = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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

        try{
            Picasso.with(context).load(imagePath).into(holder.gridMovieImage);
        }catch (Exception e){

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
