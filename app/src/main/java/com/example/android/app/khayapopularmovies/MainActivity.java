package com.example.android.app.khayapopularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {


    private String url;
    private TextView textView;
    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;
    ArrayList movieList;
    private final String TOP_RATED = "top_rated";
    private final String POPULAR = "popular";
    private ProgressBar mLoadingIndicatore;
    private TextView mErrorMessageDisplay;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicatore = (ProgressBar) findViewById(R.id.progress);

        mErrorMessageDisplay = (TextView) findViewById(R.id.error);

        mMovieList = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager gridLayout = new GridLayoutManager(this, 3);
        //TODO EXCELLENT You're using a GridLayoutManager to display the thumbnails

        mMovieList.setLayoutManager(gridLayout);

        mMovieList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this, this);

        mMovieList.setAdapter(mAdapter);

        loadMovieData();

    }

    private void loadMovieData() {
        showMovieDataView();

        new FetchMoviesTask().execute(POPULAR);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mMovieList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMovieList.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>>{
    //TODO SUGGESTION Use AsyncTaskLoader, it is more efficient than AsyncTask as discussed in class
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicatore.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }

            URL movieRequest = NetworkUtils.buildUrl(params[0]);
            try{
                url = NetworkUtils.getResponseFromHttpUrl(movieRequest);
                // TODO SUGGESTION Use meaningful & unambiguous identifier names.
                // TODO SUGGESTION 'url' is a very misleading identifier name, the string 'url' actually contains JSON data
                ArrayList movies = OpenMovieJsonUtils.getSimpleMovieStrings(MainActivity.this, url);
                movieList = movies;
                return movies;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
            //TODO AWESOME You're doing all the heavy lifting on a background thread, leaving the UI thread free => UX++
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mLoadingIndicatore.setVisibility(View.INVISIBLE);
            if(movies != null){
                showMovieDataView();
                mAdapter.setMovieData(movies);
            }else {
                showErrorMessage();
            }
        }
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
        //COMPLETED REQUIRED String literals should be in strings.xml or defined as constants; improves localisation & maintenance, less error prone.

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
                new FetchMoviesTask().execute(POPULAR); //TODO EXCELLENT Good use of String constants here
                return true;

            case R.id.top_rated:
                mAdapter.setMovieData(null);
                new FetchMoviesTask().execute(TOP_RATED);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
