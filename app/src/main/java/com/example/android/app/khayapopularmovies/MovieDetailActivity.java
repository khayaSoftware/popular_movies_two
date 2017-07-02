package com.example.android.app.khayapopularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.app.khayapopularmovies.data.ContractFavoriteMovie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewDescription;
    private TextView textViewStatus;
    private TextView textViewTitle;
    private String imagePath;
    private Bundle extras;
    FloatingActionButton fab;
    static final String PARTIAL_IMAGE_LINK = "http://image.tmdb.org/t/p/w780/";
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

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
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_DESCRIPTION, extras.getString(getString(R.string.bundle_description)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_RELEASE_DATE, extras.getString(getString(R.string.bundle_release_date)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, extras.getString(getString(R.string.bundle_vote_average)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, extras.getString(getString(R.string.bundle_vote_count)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_TITLE, extras.getString(getString(R.string.bundle_title)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_BACKDROP_URL, extras.getString(getString(R.string.bundle_title)));
                    contentValues.put(ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_POSTER_URL, extras.getString(getString(R.string.bundle_poster_url)));

                    Uri uri = getContentResolver().insert(ContractFavoriteMovie.FavoriteMovieEntry.CONTENT_URI, contentValues);

                    if (uri != null) {
                        Snackbar.make(view, extras.getString(getString(R.string.bundle_title)) + " " + getString(R.string.save_message), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.action_type), null).show();
                    }

                    fab.setImageResource(R.drawable.ic_check_black_24dp);
                }
            });


    }


}
