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

import static java.security.AccessController.getContext;

/**
 * Created by User on 5/16/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private int mMoviesItems;
    private  Context context;
    public MovieAdapter(int numberOfMovies, Context context){

        mMoviesItems = numberOfMovies;
        this.context = context;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView gridMovieItem;
        ImageView gridMovieImage;

        public MovieViewHolder(View itemView){
            super(itemView);

            gridMovieItem = (TextView) itemView.findViewById(R.id.tv_item_movie);
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
        holder.gridMovieItem.setText(""+position);
        try{
            Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(holder.gridMovieImage);
        }catch (Exception e){
            Log.d(TAG, "error");
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mMoviesItems;
    }


}
