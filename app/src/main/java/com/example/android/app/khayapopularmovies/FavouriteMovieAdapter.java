package com.example.android.app.khayapopularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by noybs on 02/07/2017.
 */
//TODO SUGGESTION This noybs chap has written large portions of your submission - or so it seems :^)

public class FavouriteMovieAdapter extends RecyclerView.Adapter<FavouriteMovieAdapter.FavouriteViewHolder> {

    private Cursor mCursor;
    public final FavouriteAdapterOnClickHandler mClickHandler;
    private ArrayList<Movie> mMovies;
    static final String TAG = MovieAdapter.class.getSimpleName();
    static final String PARTIAL_IMAGE_LINK = "http://image.tmdb.org/t/p/w185/";
    private int mMoviesItems;
    private Context mContext;


    public FavouriteMovieAdapter(Context mContext, Cursor cursor, FavouriteAdapterOnClickHandler clickHandler){
        this.mContext = mContext;
        mMoviesItems = cursor.getCount();
        mClickHandler = clickHandler;
    }

    public interface FavouriteAdapterOnClickHandler{
        void onClick(Movie movie);
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int adapterPostition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPostition);
            mClickHandler.onClick(movie);
        }

        ImageView gridMovieImage;

        public FavouriteViewHolder(View itemView) {
            super(itemView);
            gridMovieImage = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutForListItem = R.layout.movie_grid_items;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmedietly = false;

        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmedietly);
        FavouriteViewHolder movieViewHolder = new FavouriteViewHolder(view);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }
        //TODO SUGGESTION Simplify this code, are the 'if' and 'return' statements necessary?

    }

    @Override
    public int getItemCount() {
        return mMoviesItems;
    }

}
