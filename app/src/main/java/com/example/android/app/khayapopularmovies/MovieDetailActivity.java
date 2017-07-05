package com.example.android.app.khayapopularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.app.khayapopularmovies.data.ContractFavoriteMovie;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<String>> {

    private ImageView imageView;
    private TextView textViewDescription;
    private TextView textViewStatus;
    private TextView textViewTitle;
    private String imagePath;
    private RecyclerView mReviewList;
    private RecyclerView mTrailerList;
    private TrailerAdapter mTrailerAdapter;
    private Bundle extras;
    FloatingActionButton fab;
    static final String PARTIAL_IMAGE_LINK = "http://image.tmdb.org/t/p/w780/";
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private ProgressBar mLoadingIndicatore;
    private String jsonData;
    private TextView mErrorMessageDisplay;
    private ReviewAdapter mReviewAdapter;
    private ArrayList<String> reviewList;
    private static final int LOADER_ID = 22;
    private final String REVIEWS = "reviews";
    private final String VIDEOS = "trailers";
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mLoadingIndicatore = (ProgressBar) findViewById(R.id.progress);
        mErrorMessageDisplay = (TextView) findViewById(R.id.error);
        imageView = (ImageView) findViewById(R.id.movie_poster);
        textViewDescription = (TextView) findViewById(R.id.tv_description);
        textViewStatus = (TextView) findViewById(R.id.tv_stats);
        textViewTitle = (TextView) findViewById(R.id.tv_title);

        Intent intentThatStarted = getIntent();

        if (intentThatStarted != null) {

            extras = getIntent().getExtras();
            textViewStatus.setText(extras.getString(getString(R.string.bundle_release_date)) + " " + getString(R.string.rating) + " " + extras.getString(getString(R.string.bundle_vote_average)) + getString(R.string.top_rate));
            textViewDescription.setText(extras.getString(getString(R.string.bundle_description)));
            textViewTitle.setText(extras.getString(getString(R.string.bundle_title)));
            imagePath = PARTIAL_IMAGE_LINK + extras.getString(getString(R.string.bundle_url));

            try {
                Picasso.with(this).load(imagePath).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null)

            if (extras.getInt(getString(R.string.bundle_is_fav)) == 0) {
                fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extras.getInt(getString(R.string.bundle_is_fav)) == 1) {

                    Snackbar.make(view, extras.getString(getString(R.string.bundle_title)) + " " + getString(R.string.delete_message), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.action_type), null).show();
                    fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Uri uri = ContractFavoriteMovie.FavoriteMovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(extras.getString(getString(R.string.bundle_id))).build();
                    getContentResolver().delete(uri, null, null);
                } else if (extras.getInt(getString(R.string.bundle_is_fav)) == 0) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_DESCRIPTION, extras.getString(getString(R.string.bundle_description)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_RELEASE_DATE, extras.getString(getString(R.string.bundle_release_date)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, extras.getString(getString(R.string.bundle_vote_average)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_VOTE_COUNT, extras.getString(getString(R.string.bundle_vote_count)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_TITLE, extras.getString(getString(R.string.bundle_title)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_BACKDROP_URL, extras.getString(getString(R.string.bundle_url)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_POSTER_URL, extras.getString(getString(R.string.bundle_poster_url)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_IS_FAVORITE, 1);

                    Uri uri = getContentResolver().insert(ContractFavoriteMovie.FavoriteMovieEntry.CONTENT_URI, contentValues);

                    if (uri != null) {
                        Snackbar.make(view, extras.getString(getString(R.string.bundle_title)) + " " + getString(R.string.save_message), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.action_type), null).show();
                    }

                    fab.setImageResource(R.drawable.ic_favorite_white_24dp);


                }
            }
        });

        mReviewList = (RecyclerView) findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutMg = new LinearLayoutManager(this);
        mReviewList.setLayoutManager(layoutMg);
        mReviewAdapter = new ReviewAdapter(this);
        mReviewList.setHasFixedSize(true);
        mReviewList.setAdapter(mReviewAdapter);
        loadMovieData(extras.getString(getString(R.string.bundle_id)), REVIEWS);

        mTrailerList = (RecyclerView) findViewById(R.id.rv_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTrailerList.setLayoutManager(layoutManager);
        mTrailerAdapter = new TrailerAdapter(this, this);
        mTrailerList.setHasFixedSize(true);
        mTrailerList.setAdapter(mTrailerAdapter);
        loadMovieData(extras.getString(getString(R.string.bundle_id)), VIDEOS);


    }

    private void showMovieDataView() {
      //  mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        //mReviewList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        //mReviewList.setVisibility(View.INVISIBLE);

        //mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void loadMovieData(String movieID, String sort) {

        bundle = new Bundle();
        bundle.putString(getString(R.string.menu_item_key), sort);
        bundle.putString(getString(R.string.movie_id_key), movieID);

        LoaderManager lm = getSupportLoaderManager();
        android.support.v4.content.Loader<ArrayList<String>> movieLoader = lm.getLoader(LOADER_ID);

        if (movieLoader == null) {
            lm.initLoader(LOADER_ID, bundle, this);
        } else {
            lm.restartLoader(LOADER_ID, bundle, this);
        }

    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<ArrayList<String>>(this) {

            @Override
            protected void onStartLoading() {

                if (args == null) {
                    return;
                }

               // mLoadingIndicatore.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public ArrayList<String> loadInBackground() {
                URL movieRequest = NetworkUtils.buildUrl(args.getString(getString(R.string.menu_item_key)), args.getString(getString(R.string.movie_id_key)));
                if(args.getString(getString(R.string.menu_item_key)).equals(REVIEWS)){
                    try {
                        jsonData = NetworkUtils.getResponseFromHttpUrl(movieRequest);
                        Log.d(TAG, "Json data = "+ jsonData);
                        ArrayList reviews = OpenMovieJsonUtils.getSimpleReviewStrings(MovieDetailActivity.this, jsonData);
                        reviewList = reviews;
                        return reviews;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }else{
                    try {
                        jsonData = NetworkUtils.getResponseFromHttpUrl(movieRequest);
                        Log.d(TAG, "Json data = "+ jsonData);
                        ArrayList reviews = OpenMovieJsonUtils.getSimpleTrailerStrings(MovieDetailActivity.this, jsonData);
                        reviewList = reviews;
                        return reviews;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }

        };


    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> movies) {
        //mLoadingIndicatore.setVisibility(View.INVISIBLE);
        if (movies != null) {
            if(bundle.getString(getString(R.string.menu_item_key)).equals(REVIEWS)){
                showMovieDataView();
                mReviewAdapter.setReviews(reviewList);
            }
            else{
                showMovieDataView();
                mTrailerAdapter.setReviews(reviewList);
            }

        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {

    }

    @Override
    public void onClick(String trailer) {

    }
}
