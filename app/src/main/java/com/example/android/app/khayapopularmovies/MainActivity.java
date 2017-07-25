package com.example.android.app.khayapopularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.app.khayapopularmovies.data.ContractFavoriteMovie;
import com.example.android.app.khayapopularmovies.data.HelperFavoriteMovie;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private String jsonData;
    private TextView textView;
    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;
    ArrayList movieList;
    private final static String TOP_RATED = "top_rated";
    private final static String POPULAR = "popular";
    private final static String FAVOURITE = "Favourites";
    private ProgressBar mLoadingIndicatore;
    private TextView mErrorMessageDisplay;
    private RecyclerView mRecyclerView;
    private static final int LOADER_ID = 22;
    private static String optionSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicatore = (ProgressBar) findViewById(R.id.progress);
        //For deleting database BE CAREFUL!!
        //HelperFavoriteMovie.deleteDatabase(this);
        mErrorMessageDisplay = (TextView) findViewById(R.id.error);

        mMovieList = (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager gridLayout;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayout = new GridLayoutManager(this, 3);
        } else {
            gridLayout = new GridLayoutManager(this, 2);
        }

        mMovieList.setLayoutManager(gridLayout);

        mMovieList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this, this);

        mMovieList.setAdapter(mAdapter);

        String currentOption = getString(R.string.empty);
        //TODO SUGGESTION You might aswell initialise currentOption to POPULAR, and remove the else{} from the if statement below
        //TODO SUGGESTION sortCriteria or something like that is more meaningful & unambiguous than "currentOption"

        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(getString(R.string.menu_item_key))){
                currentOption = savedInstanceState.getString(getString(R.string.menu_item_key));
            }
            //TODO SUGGESTION You can rewrite the nested if statements as if(savedInstanceState!=null && savedInstanceState.containsKey(getString(R.string.menu_item_key))){...} else{...}
        }else {
            currentOption = POPULAR;
            optionSelected = POPULAR;
        }

        loadMovieData(currentOption);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentState = getString(R.string.empty);
        //TODO SUGGESTION sortCriteria or something like that is more meaningful & unambiguous than "currentState"
        //TODO SUGGESTION Try to be consistent with naming variables "currentState" is used here, but "currentOption" is used in onCreate()
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.containsKey(getString(R.string.menu_item_key))){
                currentState = bundle.getString(getString(R.string.menu_item_key));
                loadMovieData(currentState);
            }
        }
        //TODO AWESOME You're saving the sort-criteria.
        //TODO REQUIREMENT The Core App Quality Guidelines require "Maintains list items positions on device rotation" , however the view returns to the top upon rotation.
        //TODO REQUIREMENT The sort criteria is not maintained when returning from MovieDetailsActivity
    }

    @Override
    protected void onStop() {
        super.onStop();
        getIntent().putExtra(getString(R.string.menu_item_key), optionSelected);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.menu_item_key), optionSelected);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mMovieList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMovieList.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setText(getString(R.string.error_msg));
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showDataErrorMessage(){
        mMovieList.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setText(getString(R.string.error_fav_msg));
        mErrorMessageDisplay.setVisibility(View.VISIBLE);

    }

    private void loadMovieData(String selectedMenuItem) {
        showMovieDataView();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.menu_item_key), selectedMenuItem);

        LoaderManager lm = getSupportLoaderManager();
        android.support.v4.content.Loader<ArrayList<Movie>> movieLoader = lm.getLoader(LOADER_ID);

        if (movieLoader == null) {
            lm.initLoader(LOADER_ID, bundle, this);
        } else {
            lm.restartLoader(LOADER_ID, bundle, this);
        }

    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
    //TODO AWESOME Nice use of Loaders to do network tasks, and JSON parsing in the background
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            @Override
            protected void onStartLoading() {

                if (args == null) {
                    return;
                }

                mLoadingIndicatore.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public ArrayList<Movie> loadInBackground() {

                if(args.getString(getString(R.string.menu_item_key)).equals(POPULAR) || args.getString(getString(R.string.menu_item_key)).equals(TOP_RATED)){
                    URL movieRequest = NetworkUtils.buildUrl(args.getString(getString(R.string.menu_item_key)));
                    try {
                        jsonData = NetworkUtils.getResponseFromHttpUrl(movieRequest);
                        ArrayList movies = OpenMovieJsonUtils.getSimpleMovieStrings(MainActivity.this, jsonData);
                        movieList = movies;
                        return movies;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }else if(args.getString(getString(R.string.menu_item_key)).equals(FAVOURITE)){
                        movieList = loadFavouriteMovies();
                        return movieList;
                }else {
                    return null;
                }

            }

        };


    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        mLoadingIndicatore.setVisibility(View.INVISIBLE);
        if (movies != null) {
            showMovieDataView();
            mAdapter.setMovieData(movies);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        //Do nothing
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destination = MovieDetailActivity.class;
        Intent intentToStartAct = new Intent(context, destination);
        Bundle extras = new Bundle();
        extras.putString(getString(R.string.bundle_url), movie.backdropPath);
        extras.putString(getString(R.string.bundle_description), movie.overview);
        extras.putString(getString(R.string.bundle_title), movie.title);
        extras.putString(getString(R.string.bundle_release_date), movie.releaseDate.substring(0, 4));
        extras.putString(getString(R.string.bundle_vote_average), movie.voteAverage);
        extras.putString(getString(R.string.bundle_poster_url), movie.posterPath);
        extras.putString(getString(R.string.bundle_vote_count), movie.voteCount);
        extras.putString(getString(R.string.bundle_id), movie.id);

        intentToStartAct.putExtras(extras);
        startActivity(intentToStartAct);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular:
                optionSelected = POPULAR;
                mAdapter.setMovieData(null);
                loadMovieData(POPULAR);
                Toast.makeText(this, getString(R.string.label_popular_movies), Toast.LENGTH_LONG).show();
                return true;

            case R.id.top_rated:
                optionSelected = TOP_RATED;
                mAdapter.setMovieData(null);
                loadMovieData(TOP_RATED);
                Toast.makeText(this, getString(R.string.label_top_rated_movies), Toast.LENGTH_LONG).show();
                return true;

            case R.id.favourites:
                optionSelected = FAVOURITE;
                mAdapter.setMovieData(null);
                loadMovieData(FAVOURITE);
                Toast.makeText(this, getString(R.string.favourite_label), Toast.LENGTH_LONG).show();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public Cursor returnFavourites() {
        return getContentResolver().query(ContractFavoriteMovie.FavoriteMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

    }

    private ArrayList<Movie> loadFavouriteMovies(){
        Cursor cursor = returnFavourites();
        cursor.moveToFirst();

        ArrayList<Movie> favoriteMovies = new ArrayList<Movie>();
        try{
            while (cursor.moveToNext()) {
                String movieName = cursor.getString(cursor.getColumnIndex(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_TITLE));
                String movieDescription = cursor.getString(cursor.getColumnIndex(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_DESCRIPTION));
                String movieRelease = cursor.getString(cursor.getColumnIndex(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_RELEASE_DATE));
                String movieID = cursor.getString(cursor.getColumnIndex(ContractFavoriteMovie.FavoriteMovieEntry.MOVIE_ID)).toString();
                //TODO SUGGESTION No need to invoke.toString() when you're calling getString()
                String movieBackdrop = cursor.getString(cursor.getColumnIndex(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_BACKDROP_URL));
                String moviePoster = cursor.getString(cursor.getColumnIndex(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_POSTER_URL));
                String movieVoteAverage = cursor.getString(cursor.getColumnIndex(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE));
                String movieVoteCount = cursor.getString(cursor.getColumnIndex(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_VOTE_COUNT));

                Movie favouriteMovie = new Movie(
                        moviePoster,
                        movieDescription,
                        movieRelease,
                        movieID,
                        movieName,
                        movieBackdrop,
                        movieVoteCount,
                        movieVoteAverage
                );

                favouriteMovie.setFavourite(1);
                favoriteMovies.add(favouriteMovie);
                //Toast.makeText(this, movieName, Toast.LENGTH_LONG).show();
            }
        }finally {
            cursor.close();
        }

        return favoriteMovies;
        //HelperFavoriteMovie.deleteDatabase(this);
        //Toast.makeText(this, updated, Toast.LENGTH_LONG).show();
    }
}
