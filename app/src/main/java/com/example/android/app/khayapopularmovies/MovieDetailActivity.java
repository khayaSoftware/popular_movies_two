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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private String jsonData;
    private ReviewAdapter mReviewAdapter;
    private ArrayList<String> reviewList;
    private ArrayList<String> trailerList;
    private static final int TRAILER_LOADER_ID = 21;
    private static final int REVIEW_LOADER_ID = 22;
    private final String REVIEWS = "reviews";
    private final String VIDEOS = "trailers";
    Bundle bundle;
    boolean isFav = false;
    Movie movie;
    TextView reviewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initFields();

        Intent intentThatStarted = getIntent();

        if (intentThatStarted != null) {
            setUpFields();
        }

        setFab();

        loadTrailers();

        loadReviews();


    }

    public void loadReviews(){
        mTrailerList = (RecyclerView) findViewById(R.id.rv_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTrailerList.setLayoutManager(layoutManager);
        mTrailerAdapter = new TrailerAdapter(this, this);
        mTrailerList.setHasFixedSize(true);
        mTrailerList.setAdapter(mTrailerAdapter);
        reviewLoader(extras.getString(getString(R.string.bundle_id)), VIDEOS);
    }

    public void loadTrailers(){
        mReviewList = (RecyclerView) findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutMg = new LinearLayoutManager(this);
        mReviewList.setLayoutManager(layoutMg);
        mReviewAdapter = new ReviewAdapter(this);
        mReviewList.setHasFixedSize(true);
        mReviewList.setAdapter(mReviewAdapter);
        trailerLoader(extras.getString(getString(R.string.bundle_id)), REVIEWS);
    }

    public void setFab(){
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
    }

    public void initFields(){
        reviewTitle = (TextView) findViewById(R.id.reviews_count);
        imageView = (ImageView) findViewById(R.id.movie_poster);
        textViewDescription = (TextView) findViewById(R.id.tv_description);
        textViewStatus = (TextView) findViewById(R.id.tv_stats);
        textViewTitle = (TextView) findViewById(R.id.tv_title);
    }

    public void setUpFields(){
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


    private void trailerLoader(String movieID, String sort) {

        bundle = new Bundle();
        bundle.putString(getString(R.string.menu_item_key), sort);
        bundle.putString(getString(R.string.movie_id_key), movieID);

        LoaderManager lm = getSupportLoaderManager();
        android.support.v4.content.Loader<ArrayList<String>> movieLoader = lm.getLoader(TRAILER_LOADER_ID);


        if (movieLoader == null) {
            lm.initLoader(TRAILER_LOADER_ID, bundle, this);
        } else {
            lm.restartLoader(TRAILER_LOADER_ID, bundle, this);
        }


    }

    private void reviewLoader(String movieID, String sort){
        bundle = new Bundle();
        bundle.putString(getString(R.string.menu_item_key), sort);
        bundle.putString(getString(R.string.movie_id_key), movieID);

        LoaderManager lm = getSupportLoaderManager();
        android.support.v4.content.Loader<ArrayList<String>> movieLoader = lm.getLoader(REVIEW_LOADER_ID);

        if (movieLoader == null) {
            lm.initLoader(REVIEW_LOADER_ID, bundle, this);
        } else {
            lm.restartLoader(REVIEW_LOADER_ID, bundle, this);
        }

    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, final Bundle args) {
        Log.d(TAG, "id = " + id);
        if(id == 21){
            return new AsyncTaskLoader<ArrayList<String>>(this) {

                @Override
                protected void onStartLoading() {
                    if (args == null) {
                        return;
                    }
                    forceLoad();
                }

                @Override
                public ArrayList<String> loadInBackground() {
                    URL movieRequest = NetworkUtils.buildUrl(args.getString(getString(R.string.menu_item_key)), args.getString(getString(R.string.movie_id_key)));
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

                }

            };
        }
        else if(id == 22){
            return new AsyncTaskLoader<ArrayList<String>>(this) {

                @Override
                protected void onStartLoading() {

                    if (args == null) {
                        return;
                    }
                    forceLoad();
                }

                @Override
                public ArrayList<String> loadInBackground() {
                    URL movieRequest = NetworkUtils.buildUrl(args.getString(getString(R.string.menu_item_key)), args.getString(getString(R.string.movie_id_key)));
                    try {
                        jsonData = NetworkUtils.getResponseFromHttpUrl(movieRequest);
                        Log.d(TAG, "Json data = "+ jsonData);
                        ArrayList trailers = OpenMovieJsonUtils.getSimpleTrailerStrings(MovieDetailActivity.this, jsonData);
                        trailerList = trailers;
                        return trailers;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                }

            };
        }
        else {
            return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> movies) {
        if (movies != null) {
            if(loader.getId() == 21){

                mReviewAdapter.setReviews(reviewList);
                reviewTitle.setText(reviewTitle.getText().toString() + " (" + reviewList.size()+")");
            }
            else if(loader.getId() == 22){

                mTrailerAdapter.setReviews(trailerList);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {

    }

    @Override
    public void onClick(String trailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer)));
    }
}
