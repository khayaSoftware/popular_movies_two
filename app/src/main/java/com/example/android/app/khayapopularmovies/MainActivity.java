package com.example.android.app.khayapopularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Movie>> {


    private String jsonData;
    private TextView textView;
    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;
    ArrayList movieList;
    private final String TOP_RATED = "top_rated";
    private final String POPULAR = "popular";
    private ProgressBar mLoadingIndicatore;
    private TextView mErrorMessageDisplay;
    private RecyclerView mRecyclerView;
    private static final int LOADER_ID = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicatore = (ProgressBar) findViewById(R.id.progress);

        mErrorMessageDisplay = (TextView) findViewById(R.id.error);

        mMovieList = (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager gridLayout;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridLayout = new GridLayoutManager(this, 3);
    }
        else {
            gridLayout = new GridLayoutManager(this, 2);
        }

        mMovieList.setLayoutManager(gridLayout);

        mMovieList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this, this);

        mMovieList.setAdapter(mAdapter);

        loadMovieData();

    }



    private void loadMovieData() {
        showMovieDataView();

        loadMovieData(POPULAR);
    }



    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mMovieList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMovieList.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void loadMovieData(String selectedMenuItem){
        
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.menu_item_key), selectedMenuItem);

        LoaderManager lm = getSupportLoaderManager();
        android.support.v4.content.Loader<Movie[]> movieLoader = lm.getLoader(LOADER_ID);

        if (movieLoader == null){
            lm.initLoader(LOADER_ID,bundle,this);
        }else{
            lm.restartLoader(LOADER_ID,bundle,this);
        }

    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {

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
        extras.putString(getString(R.string.bundle_url),movie.backdropPath);
        extras.putString(getString(R.string.bundle_description),movie.overview);
        extras.putString(getString(R.string.bundle_title), movie.title);
        extras.putString(getString(R.string.bundle_release_date), movie.releaseDate.substring(0,4));
        extras.putString(getString(R.string.bundle_vote_average), movie.voteAverage);

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
        switch (id){
            case R.id.popular:
                mAdapter.setMovieData(null);
                loadMovieData(POPULAR);
                return true;

            case R.id.top_rated:
                mAdapter.setMovieData(null);
                loadMovieData(TOP_RATED);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
